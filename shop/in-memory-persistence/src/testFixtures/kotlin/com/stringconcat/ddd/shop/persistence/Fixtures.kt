package com.stringconcat.ddd.shop.persistence

import arrow.core.Either
import com.stringconcat.ddd.common.events.DomainEventPublisher
import com.stringconcat.ddd.common.types.base.DomainEvent
import com.stringconcat.ddd.shop.domain.cart
import com.stringconcat.ddd.shop.domain.cart.Cart
import com.stringconcat.ddd.shop.domain.meal
import com.stringconcat.ddd.shop.domain.mealId
import com.stringconcat.ddd.shop.domain.menu.Meal
import com.stringconcat.ddd.shop.domain.menu.MealId
import com.stringconcat.ddd.shop.domain.order
import com.stringconcat.ddd.shop.domain.order.OrderState
import com.stringconcat.ddd.shop.domain.order.ShopOrder
import com.stringconcat.ddd.shop.domain.order.ShopOrderId
import com.stringconcat.ddd.shop.domain.orderId

fun mealWithEvents(id: MealId = mealId()): Meal {
    val meal = meal(id)
    meal.removeMealFromMenu()
    return meal
}

fun cartWithEvents(): Cart {
    val cart = cart()
    cart.addMeal(meal())
    return cart
}

fun orderReadyForComplete(id: ShopOrderId = orderId()) = order(state = OrderState.CONFIRMED, id = id)

fun orderWithEvents(id: ShopOrderId = orderId()): ShopOrder {
    val order = orderReadyForComplete(id)

    check(order.complete() is Either.Right<Unit>)
    return order
}

class TestEventPublisher : DomainEventPublisher {
    val storage = ArrayList<DomainEvent>()
    override fun publish(events: Collection<DomainEvent>) {
        storage.addAll(events)
    }
}