package com.stringconcat.ddd.kitchen.usecase.order.access

import com.stringconcat.ddd.kitchen.domain.order.KitchenOrder

interface KitchenOrderPersister {
    fun save(order: KitchenOrder)
}