package com.stringconcat.ddd.shop.domain

import arrow.core.Either
import com.stringconcat.ddd.common.types.base.Version
import com.stringconcat.ddd.common.types.count
import com.stringconcat.ddd.common.types.common.Address
import com.stringconcat.ddd.common.types.common.Count
import com.stringconcat.ddd.shop.domain.cart.Cart
import com.stringconcat.ddd.shop.domain.cart.CartId
import com.stringconcat.ddd.shop.domain.cart.CartRestorer
import com.stringconcat.ddd.shop.domain.cart.CustomerId
import com.stringconcat.ddd.shop.domain.menu.Meal
import com.stringconcat.ddd.shop.domain.menu.MealDescription
import com.stringconcat.ddd.shop.domain.menu.MealId
import com.stringconcat.ddd.shop.domain.menu.MealName
import com.stringconcat.ddd.shop.domain.menu.MealRestorer
import com.stringconcat.ddd.shop.domain.menu.Price
import com.stringconcat.ddd.shop.domain.order.ShopOrder
import com.stringconcat.ddd.shop.domain.order.ShopOrderId
import com.stringconcat.ddd.shop.domain.order.OrderItem
import com.stringconcat.ddd.shop.domain.order.ShopOrderRestorer
import com.stringconcat.ddd.shop.domain.order.OrderState
import com.stringconcat.ddd.shop.domain.order.CustomerHasActiveOrder
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
    return result.value
}

fun mealName(name: String = "Name ${Random.nextInt()}"): MealName {
    val result = MealName.from(name)
    check(result is Either.Right<MealName>)
    return result.value
}

fun mealDescription(description: String = "Description ${Random.nextInt()}"): MealDescription {
    val result = MealDescription.from(description)
    check(result is Either.Right<MealDescription>)
    return result.value
}

fun price(value: BigDecimal = BigDecimal(Random.nextInt(1, 500000))): Price {
    val result = Price.from(value)
    check(result is Either.Right<Price>)
    return result.value
}

fun version() = Version.new()

fun mealId(id: Long = Random.nextLong()) = MealId(id)

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

fun customerId() = CustomerId(UUID.randomUUID().toString())

fun cartId() = CartId(Random.nextLong())

fun cart(meals: Map<MealId, Count> = emptyMap(),
         customerId: CustomerId = customerId()): Cart {
    return CartRestorer.restoreCart(
        id = cartId(),
        forCustomer = customerId,
        created = OffsetDateTime.now(),
        meals = meals,
        version = version()
    )
}

fun orderId() = ShopOrderId(Random.nextLong())

fun orderItem(
    price: Price = price(),
    count: Count = count()
): OrderItem {
    return OrderItem(
        mealId = mealId(),
        price = price,
        count = count
    )
}

fun order(
    id: ShopOrderId = orderId(),
    customerId: CustomerId = customerId(),
    state: OrderState = OrderState.COMPLETED,
    orderItems: Set<OrderItem> = setOf(orderItem()),
): ShopOrder {
    return ShopOrderRestorer.restoreOrder(
        id = id,
        created = OffsetDateTime.now(),
        forCustomer = customerId,
        orderItems = orderItems,
        address = address(),
        state = state,
        version = version()
    )
}

class TestCustomerHasActiveOrder(val hasActive: Boolean) : CustomerHasActiveOrder {
    override fun check(forCustomer: CustomerId): Boolean {
        return hasActive
    }
}