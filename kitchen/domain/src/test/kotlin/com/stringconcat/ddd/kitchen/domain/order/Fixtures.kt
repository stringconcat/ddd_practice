package com.stringconcat.ddd.kitchen.domain.order

import arrow.core.Either
import com.stringconcat.ddd.common.types.base.Version
import com.stringconcat.ddd.common.types.common.Count
import kotlin.random.Random

fun orderId() = KitchenOrderId(Random.nextLong())

fun version() = Version.generate()

fun count(value: Int = Random.nextInt(20, 5000)): Count {
    val result = Count.from(value)
    check(result is Either.Right<Count>)
    return result.b
}

fun meal(): Meal {
    val result = Meal.from("Meal #${Random.nextInt()}")
    check(result is Either.Right<Meal>)
    return result.b
}

fun orderItem(): OrderItem {
    return OrderItem(
        meal = meal(),
        count = count()
    )
}

fun order(cooked: Boolean = true): KitchenOrder {
    return KitchenOrderRestorer.restoreOrder(
        id = orderId(),
        orderItems = setOf(orderItem()),
        cooked = cooked,
        version = version()
    )
}