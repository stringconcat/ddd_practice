package com.stringconcat.ddd.kitchen.usecase.order

import com.stringconcat.ddd.kitchen.domain.order.KitchenOrderId
import com.stringconcat.ddd.kitchen.domain.order.OrderItem

interface GetOrders {

    fun execute(): List<KitchenOrderInfo>
}

data class KitchenOrderInfo(
    val id: KitchenOrderId,
    val cooked: Boolean,
    val meals: List<OrderItem>
)