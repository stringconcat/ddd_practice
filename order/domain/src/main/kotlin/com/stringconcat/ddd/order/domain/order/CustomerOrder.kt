package com.stringconcat.ddd.order.domain.order

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.stringconcat.ddd.common.types.base.AggregateRoot
import com.stringconcat.ddd.common.types.base.DomainEvent
import com.stringconcat.ddd.common.types.base.Version
import com.stringconcat.ddd.common.types.common.Count
import com.stringconcat.ddd.order.domain.cart.Cart
import com.stringconcat.ddd.order.domain.cart.CustomerId
import com.stringconcat.ddd.order.domain.rules.CustomerHasActiveOrderRule
import com.stringconcat.ddd.order.domain.menu.MealId
import com.stringconcat.ddd.order.domain.menu.Price
import com.stringconcat.ddd.order.domain.providers.MealPriceProvider
import java.time.OffsetDateTime

class CustomerOrder internal constructor(
    id: CustomerOrderId,
    val created: OffsetDateTime,
    val customerId: CustomerId,
    val orderItems: Set<OrderItem>,
    version: Version
) : AggregateRoot<CustomerOrderId>(id, version) {

    internal var state: OrderState = OrderState.WAITING_FOR_PAYMENT

    companion object {

        fun checkout(
            cart: Cart,
            idGenerator: CustomerOrderIdGenerator,
            activeOrder: CustomerHasActiveOrderRule,
            priceProvider: MealPriceProvider
        ): Either<CheckoutError, CustomerOrder> {

            if (activeOrder.hasActiveOrder(cart.customerId)) {
                return CheckoutError.AlreadyHasActiveOrder.left()
            }

            val meals = cart.meals()

            return if (meals.isNotEmpty()) {

                val items = meals.map {
                    val mealId = it.key
                    val count = it.value
                    val price = priceProvider.price(mealId)
                    OrderItem(mealId, price, count)
                }.toSet()

                val id = idGenerator.generate()
                CustomerOrder(
                    id = id,
                    created = OffsetDateTime.now(),
                    customerId = cart.customerId,
                    orderItems = items,
                    version = Version.generate()
                ).apply { addEvent(CustomerOrderHasBeenCreatedEvent(id)) }.right()
            } else {
                CheckoutError.EmptyCart.left()
            }
        }
    }

    fun confirm() = changeState(OrderState.CONFIRMED, CustomerOrderHasBeenConfirmedEvent(id))

    fun pay() = changeState(OrderState.PAID, CustomerOrderHasBeenPaidEvent(id))

    fun complete() = changeState(OrderState.COMPLETED, CustomerOrderHasBeenCompletedEvent(id))

    fun cancel() = changeState(OrderState.CANCELLED, CustomerOrderHasBeenCancelledEvent(id))

    private fun changeState(newState: OrderState, event: DomainEvent): Either<InvalidState, Unit> {

        return when {

            state == newState -> Unit.right()

            state.canChangeTo(newState) -> {
                state = newState
                addEvent(event)
                Unit.right()
            }

            else -> InvalidState.left()
        }
    }

    fun totalPrice(): Price {
        return orderItems
            .map { it.price.multiple(it.count) }
            .fold(Price.zero(), { acc, it -> acc.add(it) })
    }

    fun isActive(): Boolean {
        return state.active
    }
}

class OrderItem internal constructor(
    val mealId: MealId,
    val price: Price,
    val count: Count
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OrderItem

        if (mealId != other.mealId) return false

        return true
    }

    override fun hashCode(): Int {
        return mealId.hashCode()
    }
}

enum class OrderState(
    val active: Boolean,
    private val nextStates: Set<OrderState> = emptySet()
) {

    CANCELLED(false),
    COMPLETED(false),
    CONFIRMED(true, setOf(COMPLETED)),
    PAID(true, setOf(CONFIRMED, CANCELLED)),
    WAITING_FOR_PAYMENT(active = true, setOf(PAID));

    fun canChangeTo(state: OrderState) = nextStates.contains(state)
}

sealed class CheckoutError {
    object EmptyCart : CheckoutError()
    object AlreadyHasActiveOrder : CheckoutError()
}

object InvalidState