package com.stringconcat.ddd.shop.domain.order

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.stringconcat.ddd.common.types.base.AggregateRoot
import com.stringconcat.ddd.common.types.base.DomainEvent
import com.stringconcat.ddd.common.types.base.Version
import com.stringconcat.ddd.common.types.common.Address
import com.stringconcat.ddd.common.types.common.Count
import com.stringconcat.ddd.common.types.error.BusinessError
import com.stringconcat.ddd.shop.domain.cart.Cart
import com.stringconcat.ddd.shop.domain.cart.CustomerId
import com.stringconcat.ddd.shop.domain.menu.MealId
import com.stringconcat.ddd.shop.domain.menu.Price
import java.time.OffsetDateTime

class ShopOrder internal constructor(
    id: ShopOrderId,
    val created: OffsetDateTime,
    val forCustomer: CustomerId,
    val address: Address,
    val orderItems: Set<OrderItem>,
    version: Version,
) : AggregateRoot<ShopOrderId>(id, version) {

    var state: OrderState = OrderState.WAITING_FOR_PAYMENT
        internal set

    companion object {

        fun checkout(
            cart: Cart,
            idGenerator: ShopOrderIdGenerator,
            activeOrder: CustomerHasActiveOrder,
            address: Address,
            priceProvider: MealPriceProvider,
        ): Either<CheckoutError, ShopOrder> {

            if (activeOrder.check(cart.forCustomer)) {
                return CheckoutError.AlreadyHasActiveOrder.left()
            }

            val meals = cart.meals()

            return if (meals.isNotEmpty()) {

                val items = meals.map {
                    val mealId = it.key
                    val count = it.value
                    val price = priceProvider.getPrice(mealId)
                    OrderItem(mealId, price, count)
                }.toSet()

                ShopOrder(
                    id = idGenerator.generate(),
                    created = OffsetDateTime.now(),
                    forCustomer = cart.forCustomer,
                    orderItems = items,
                    address = address,
                    version = Version.new()
                ).apply {
                    addEvent(ShopOrderCreatedDomainEvent(id, cart.forCustomer))
                }.right()
            } else {
                CheckoutError.EmptyCart.left()
            }
        }
    }

    fun confirm() = changeState(OrderState.CONFIRMED, ShopOrderConfirmedDomainEvent(id))

    fun pay() = changeState(OrderState.PAID, ShopOrderPaidDomainEvent(id))

    fun complete() = changeState(OrderState.COMPLETED, ShopOrderCompletedDomainEvent(id))

    fun cancel() = changeState(OrderState.CANCELLED, ShopOrderCancelledDomainEvent(id))

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
            .fold(Price.zero()) { acc, it -> acc.add(it) }
    }

    fun isActive(): Boolean = state.active

    fun readyForConfirmOrCancel() = state == OrderState.PAID
}

class OrderItem constructor(
    val mealId: MealId,
    val price: Price,
    val count: Count,
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
    private val nextStates: Set<OrderState> = emptySet(),
) {

    CANCELLED(active = false),
    COMPLETED(active = false),
    CONFIRMED(active = true, nextStates = setOf(COMPLETED)),
    PAID(active = true, nextStates = setOf(CONFIRMED, CANCELLED)),
    WAITING_FOR_PAYMENT(active = true, nextStates = setOf(PAID));

    fun canChangeTo(state: OrderState) = nextStates.contains(state)
}

sealed class CheckoutError : BusinessError {
    object EmptyCart : CheckoutError()
    object AlreadyHasActiveOrder : CheckoutError()
}

object InvalidState : BusinessError