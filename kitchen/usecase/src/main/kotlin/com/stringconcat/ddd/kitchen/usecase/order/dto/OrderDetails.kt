package com.stringconcat.ddd.kitchen.usecase.order.dto

import com.stringconcat.ddd.kitchen.domain.order.KitchenOrder
import com.stringconcat.ddd.kitchen.domain.order.KitchenOrderId
import com.stringconcat.ddd.kitchen.domain.order.OrderItem

data class OrderDetails(
    val id: KitchenOrderId,
    val cooked: Boolean,
    val meals: List<OrderItem>
)

fun KitchenOrder.toDetails(): OrderDetails {
    return OrderDetails(
        id = this.id,
        cooked = this.cooked,
        meals = this.meals,
    )
}