package com.stringconcat.ddd.kitchen.usecase.order

import com.stringconcat.ddd.kitchen.domain.order.KitchenOrder
import com.stringconcat.ddd.kitchen.domain.order.KitchenOrderId
import com.stringconcat.ddd.kitchen.usecase.order.access.KitchenOrderExtractor
import com.stringconcat.ddd.kitchen.usecase.order.access.KitchenOrderPersister

class TestKitchenOrderPersister : KitchenOrderPersister, HashMap<KitchenOrderId, KitchenOrder>() {
    override fun save(order: KitchenOrder) {
        this[order.id] = order
    }
}

class TestKitchenOrderExtractor : KitchenOrderExtractor, HashMap<KitchenOrderId, KitchenOrder>() {
    override fun getById(orderId: KitchenOrderId) = this[orderId]

    override fun getAll() = values.toList()
}