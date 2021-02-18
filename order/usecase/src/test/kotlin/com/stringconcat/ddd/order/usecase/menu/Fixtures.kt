package com.stringconcat.ddd.order.usecase.menu

import arrow.core.Either
import com.stringconcat.ddd.common.types.base.Version
import com.stringconcat.ddd.common.types.common.Address
import com.stringconcat.ddd.common.types.common.Count
import com.stringconcat.ddd.order.domain.cart.Cart
import com.stringconcat.ddd.order.domain.cart.CartId
import com.stringconcat.ddd.order.domain.cart.CartRestorer
import com.stringconcat.ddd.order.domain.cart.CustomerId
import com.stringconcat.ddd.order.domain.menu.Meal
import com.stringconcat.ddd.order.domain.menu.MealDescription
import com.stringconcat.ddd.order.domain.menu.MealId
import com.stringconcat.ddd.order.domain.menu.MealName
import com.stringconcat.ddd.order.domain.menu.MealRestorer
import com.stringconcat.ddd.order.domain.menu.Price
import com.stringconcat.ddd.order.domain.order.CustomerOrder
import com.stringconcat.ddd.order.domain.order.CustomerOrderId
import com.stringconcat.ddd.order.domain.order.OrderItem
import com.stringconcat.ddd.order.domain.order.CustomerOrderRestorer
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

fun version() = Version.generate()

fun mealId() = MealId(Random.nextLong())

fun meal(removed: Boolean = false): Meal {

    return MealRestorer.restoreMeal(
        id = mealId(),
        name = mealName(),
        removed = removed,
        description = mealDescription(),
        price = price(),
        version = version()
    )
}

fun customerId() = CustomerId(UUID.randomUUID().toString())

fun cartId() = CartId(Random.nextLong())

fun count(value: Int = Random.nextInt(20, 5000)): Count {
    val result = Count.from(value)
    check(result is Either.Right<Count>)
    return result.b
}

fun cart(meals: Map<MealId, Count> = emptyMap()): Cart {
    return CartRestorer.restoreCart(
        id = cartId(),
        customerId = customerId(),
        created = OffsetDateTime.now(),
        meals = meals,
        version = version()
    )
}

fun orderId() = CustomerOrderId(Random.nextLong())

fun order(
    state: OrderState = OrderState.COMPLETED,
    orderItems: Set<OrderItem> = emptySet(),
): CustomerOrder {
    return CustomerOrderRestorer.restoreOrder(
        id = orderId(),
        created = OffsetDateTime.now(),
        customerId = customerId(),
        orderItems = orderItems,
        address = address(),
        state = state,
        version = version()
    )
}