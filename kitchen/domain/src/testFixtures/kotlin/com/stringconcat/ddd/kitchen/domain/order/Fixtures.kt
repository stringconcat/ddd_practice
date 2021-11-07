package com.stringconcat.ddd.kitchen.domain.order

import arrow.core.Either
import com.stringconcat.ddd.common.types.base.Version
import com.stringconcat.ddd.common.types.count
import com.stringconcat.ddd.common.types.faker
import kotlin.random.Random

fun orderId() = KitchenOrderId(Random.nextLong())

fun version() = Version.new()

fun meal(): Meal {
    val result = Meal.from(faker.food().dish())
    check(result is Either.Right<Meal>)
    return result.value
}

fun orderItem(): OrderItem {
    return OrderItem(
        meal = meal(),
        count = count()
    )
}

fun order(id: KitchenOrderId = orderId(), cooked: Boolean = true): KitchenOrder {
    return KitchenOrderRestorer.restoreOrder(
        id = id,
        orderItems = listOf(orderItem()),
        cooked = cooked,
        version = version()
    )
}