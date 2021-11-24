package com.stringconcat.ddd.shop.domain

import arrow.core.getOrElse
import com.stringconcat.ddd.common.types.base.Version
import com.stringconcat.ddd.common.types.common.Address
import com.stringconcat.ddd.common.types.common.Count
import com.stringconcat.ddd.common.types.count
import com.stringconcat.ddd.common.types.faker
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
import com.stringconcat.ddd.shop.domain.order.CustomerHasActiveOrder
import com.stringconcat.ddd.shop.domain.order.OrderItem
import com.stringconcat.ddd.shop.domain.order.OrderState
import com.stringconcat.ddd.shop.domain.order.ShopOrder
import com.stringconcat.ddd.shop.domain.order.ShopOrderId
import com.stringconcat.ddd.shop.domain.order.ShopOrderRestorer
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.UUID
import kotlin.math.absoluteValue
import kotlin.random.Random

fun address() = Address.from(
    street = faker.address().streetName(),
    building = faker.address().streetAddressNumber().toInt() + 1
).getOrElse { error("Address should be right") }

fun mealName(name: String = "${faker.food().dish()} [${Random.nextInt()}]") =
    MealName.from(name).getOrElse { error("MealName should be right") }

fun mealDescription(description: String = faker.food().ingredient()) =
    MealDescription.from(description).getOrElse { error("MealDescription should be right") }

fun price(value: BigDecimal = BigDecimal(Random.nextInt(1, 500000))) =
    Price.from(value.setScale(2)).getOrElse { error("price must be right") }

fun version() = Version.new()

fun mealId(id: Long = faker.number().randomNumber().absoluteValue) = MealId(id)

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

fun cartId() = CartId(faker.number().randomNumber())

fun cart(
    meals: Map<MealId, Count> = emptyMap(),
    customerId: CustomerId = customerId(),
): Cart {
    return CartRestorer.restoreCart(
        id = cartId(),
        forCustomer = customerId,
        created = OffsetDateTime.now(),
        meals = meals,
        version = version()
    )
}

fun orderId() = ShopOrderId(faker.number().randomNumber())

fun orderItem(
    price: Price = price(),
    count: Count = count(),
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