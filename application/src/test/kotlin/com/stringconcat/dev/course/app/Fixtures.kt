package com.stringconcat.dev.course.app

import arrow.core.Either
import com.stringconcat.ddd.common.types.base.Version
import com.stringconcat.ddd.common.types.common.Address
import com.stringconcat.ddd.common.types.common.Count
import com.stringconcat.ddd.shop.domain.cart.CustomerId
import com.stringconcat.ddd.shop.domain.menu.Meal
import com.stringconcat.ddd.shop.domain.menu.MealDescription
import com.stringconcat.ddd.shop.domain.menu.MealId
import com.stringconcat.ddd.shop.domain.menu.MealName
import com.stringconcat.ddd.shop.domain.menu.MealRestorer
import com.stringconcat.ddd.shop.domain.menu.Price
import com.stringconcat.ddd.shop.domain.order.ShopOrder
import com.stringconcat.ddd.shop.domain.order.ShopOrderId
import com.stringconcat.ddd.shop.domain.order.ShopOrderRestorer
import com.stringconcat.ddd.shop.domain.order.OrderItem
import com.stringconcat.ddd.shop.domain.order.OrderState
import com.stringconcat.ddd.shop.usecase.menu.MealExtractor
import com.stringconcat.ddd.shop.usecase.order.ShopOrderExtractor
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.LinkedHashMap
import java.util.UUID
import kotlin.math.absoluteValue
import kotlin.random.Random

fun mealName(name: String = "Name ${Random.nextInt()}"): MealName {
    val result = MealName.from(name)
    check(result is Either.Right<MealName>)
    return result.b
}

fun orderId() = ShopOrderId(Random.nextLong())

fun mealDescription(description: String = "Description ${Random.nextInt()}"): MealDescription {
    val result = MealDescription.from(description)
    check(result is Either.Right<MealDescription>)
    return result.b
}

fun price(value: BigDecimal = BigDecimal(Random.nextInt(1, 500000))): Price {
    val result = Price.from(value)
    check(result is Either.Right<Price>)
    return result.b
}

fun mealId(id: Long = Random.nextLong()) = MealId(id)

fun count(value: Int = Random.nextInt(20, 5000)): Count {
    val result = Count.from(value)
    check(result is Either.Right<Count>)
    return result.b
}

fun customerId() = CustomerId(UUID.randomUUID().toString())

fun address(): Address {

    val result = Address.from(
        street = "${Random.nextInt()}th ave",
        building = Random.nextInt().absoluteValue
    )

    check(result is Either.Right<Address>)
    return result.b
}

fun shopOrder(
    state: OrderState = OrderState.COMPLETED,
    orderItems: Set<OrderItem> = emptySet(),
): ShopOrder {
    return ShopOrderRestorer.restoreOrder(
        id = orderId(),
        created = OffsetDateTime.now(),
        forCustomer = customerId(),
        orderItems = orderItems,
        address = address(),
        state = state,
        version = Version.new()
    )
}

fun meal(removed: Boolean = false): Meal {

    return MealRestorer.restoreMeal(
        id = mealId(),
        name = mealName(),
        removed = removed,
        description = mealDescription(),
        price = price(),
        version = Version.new()
    )
}

class TestShopOrderExtractor : ShopOrderExtractor, LinkedHashMap<ShopOrderId, ShopOrder>() {
    override fun getById(orderId: ShopOrderId) = this[orderId]

    override fun getLastOrder(forCustomer: CustomerId): ShopOrder? {
        return this.values.lastOrNull { it.forCustomer == forCustomer }
    }

    override fun getAll() = values.toList()
}

class TestMealExtractor : HashMap<MealId, Meal>(), MealExtractor {
    override fun getById(id: MealId) = this[id]

    override fun getByName(name: MealName): Meal? {
        return values.firstOrNull { it.name == name }
    }

    override fun getAll() = this.values.toList()
}