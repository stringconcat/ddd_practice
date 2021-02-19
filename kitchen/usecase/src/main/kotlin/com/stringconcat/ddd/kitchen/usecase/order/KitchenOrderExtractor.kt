package com.stringconcat.ddd.kitchen.usecase.order

import com.stringconcat.ddd.kitchen.domain.order.KitchenOrder
import com.stringconcat.ddd.kitchen.domain.order.KitchenOrderId

interface KitchenOrderExtractor {
    fun getById(orderId: KitchenOrderId): KitchenOrder?
}