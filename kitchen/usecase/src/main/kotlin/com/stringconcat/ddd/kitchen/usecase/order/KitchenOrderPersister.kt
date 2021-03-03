package com.stringconcat.ddd.kitchen.usecase.order

import com.stringconcat.ddd.kitchen.domain.order.KitchenOrder

interface KitchenOrderPersister {
    fun save(order: KitchenOrder)
}