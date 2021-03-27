package com.stringconcat.ddd.order.persistence

import arrow.core.Either
import com.stringconcat.ddd.common.types.base.DomainEvent
import com.stringconcat.ddd.common.types.base.EventPublisher
import com.stringconcat.ddd.common.types.base.Version
import com.stringconcat.ddd.common.types.common.Address
import com.stringconcat.ddd.common.types.common.Count
import com.stringconcat.ddd.order.domain.cart.Cart
import com.stringconcat.ddd.order.domain.cart.CartId
import com.stringconcat.ddd.order.domain.cart.CartRestorer
import com.stringconcat.ddd.order.domain.cart.CustomerId
import com.stringconcat.ddd.order.domain.cart.NumberOfMealsExceedsLimit
import com.stringconcat.ddd.order.domain.menu.Meal
import com.stringconcat.ddd.order.domain.menu.MealDescription
import com.stringconcat.ddd.order.domain.menu.MealId
import com.stringconcat.ddd.order.domain.menu.MealName
import com.stringconcat.ddd.order.domain.menu.MealRestorer
import com.stringconcat.ddd.order.domain.menu.Price
import com.stringconcat.ddd.order.domain.order.CustomerOrder
import com.stringconcat.ddd.order.domain.order.CustomerOrderId
import com.stringconcat.ddd.order.domain.order.CustomerOrderRestorer
import com.stringconcat.ddd.order.domain.order.OrderItem
import com.stringconcat.ddd.order.domain.order.OrderState
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.UUID
import kotlin.math.absoluteValue
import kotlin.random.Random

fun address(): Address {

    val result = Address.from(
        street = "${Random.nextInt()}th ave",
        building = Random.nextInt().absoluteValue
    )

    check(result is Either.Right<Address>)
    return result.b
}

fun mealName(): MealName {
    val result = MealName.from("Name ${Random.nextInt()}")
    check(result is Either.Right<MealName>)
    return result.b
}

fun mealDescription(): MealDescription {
    val result = MealDescription.from("Description ${Random.nextInt()}")
    check(result is Either.Right<MealDescription>)
    return result.b
}

fun price(value: BigDecimal = BigDecimal(Random.nextInt(1, 500000))): Price {
    val result = Price.from(value)
    check(result is Either.Right<Price>)
    return result.b
}

fun version() = Version.new()

fun mealId() = MealId(Random.nextLong())

fun meal(id: MealId = mealId(), removed: Boolean = false): Meal {

    return MealRestorer.restoreMeal(
        id = id,
        name = mealName(),
        removed = removed,
        description = mealDescription(),
        price = price(),
        version = version()
    )
}

fun mealWithEvents(id: MealId = mealId()): Meal {
    val meal = meal(id)
    meal.removeMealFromMenu()
    return meal
}

fun removedMeal() = meal(removed = true)

fun customerId() = CustomerId(UUID.randomUUID().toString())

fun cartId() = CartId(Random.nextLong())

fun count(value: Int = Random.nextInt(20, 5000)): Count {
    val result = Count.from(value)
    check(result is Either.Right<Count>)
    return result.b
}

fun cart(
    meals: Map<MealId, Count> = emptyMap(),
    customerId: CustomerId = customerId()
): Cart {
    return CartRestorer.restoreCart(
        id = cartId(),
        forCustomer = customerId,
        created = OffsetDateTime.now(),
        meals = meals,
        version = version()
    )
}

fun cartWithEvents(): Cart {
    val cart = cart()
    cart.addMeal(meal(), NumberOfMealsDoesNotExceedLimit)
    return cart
}

fun orderId() = CustomerOrderId(Random.nextLong())

fun orderReadyForComplete(id: CustomerOrderId = orderId()) = order(state = OrderState.CONFIRMED, id = id)

fun orderWithEvents(id: CustomerOrderId = orderId()): CustomerOrder {
    val order = orderReadyForComplete(id)

    check(order.complete() is Either.Right<Unit>)
    return order
}

fun order(
    state: OrderState = OrderState.COMPLETED,
    orderItems: Set<OrderItem> = emptySet(),
    id: CustomerOrderId = orderId(),
    customerId: CustomerId = customerId()
): CustomerOrder {
    return CustomerOrderRestorer.restoreOrder(
        id = id,
        created = OffsetDateTime.now(),
        forCustomer = customerId,
        orderItems = orderItems,
        address = address(),
        state = state,
        version = version()
    )
}

class TestEventPublisher : EventPublisher {
    internal val storage = ArrayList<DomainEvent>()
    override fun publish(events: Collection<DomainEvent>) {
        storage.addAll(events)
    }
}

object NumberOfMealsDoesNotExceedLimit : NumberOfMealsExceedsLimit {
    override fun check(cart: Cart): Boolean {
        return false
    }
}
