package com.stringconcat.ddd.kitchen.persistence.order

import arrow.core.Either
import com.stringconcat.ddd.common.types.base.DomainEvent
import com.stringconcat.ddd.common.types.base.EventPublisher
import com.stringconcat.ddd.common.types.base.Version
import com.stringconcat.ddd.common.types.common.Count
import com.stringconcat.ddd.kitchen.domain.order.KitchenOrder
import com.stringconcat.ddd.kitchen.domain.order.KitchenOrderId
import com.stringconcat.ddd.kitchen.domain.order.KitchenOrderRestorer
import com.stringconcat.ddd.kitchen.domain.order.Meal
import com.stringconcat.ddd.kitchen.domain.order.OrderItem
import kotlin.random.Random

fun orderId() = KitchenOrderId(Random.nextLong())

fun version() = Version.new()

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

fun orderWithEvents(id: KitchenOrderId = orderId()): KitchenOrder {
    return order(id, false).apply {
        cook()
    }
}

fun order(id: KitchenOrderId = orderId(), cooked: Boolean = true): KitchenOrder {
    return KitchenOrderRestorer.restoreOrder(
        id = id,
        orderItems = listOf(orderItem()),
        cooked = cooked,
        version = version()
    )
}

class TestEventPublisher : EventPublisher {
    internal val storage = ArrayList<DomainEvent>()
    override fun publish(events: Collection<DomainEvent>) {
        storage.addAll(events)
    }
}
