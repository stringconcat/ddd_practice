package com.stringconcat.ddd.kitchen.persistence.order

import com.stringconcat.ddd.common.types.base.EventPublisher
import com.stringconcat.ddd.kitchen.domain.order.KitchenOrder
import com.stringconcat.ddd.kitchen.domain.order.KitchenOrderId
import com.stringconcat.ddd.kitchen.usecase.order.KitchenOrderExtractor
import com.stringconcat.ddd.kitchen.usecase.order.KitchenOrderPersister

class InMemoryKitchenOrderRepository(private val eventPublisher: EventPublisher) :
    KitchenOrderExtractor,
    KitchenOrderPersister {

    internal val storage = LinkedHashMap<KitchenOrderId, KitchenOrder>()

    override fun getById(orderId: KitchenOrderId) = storage[orderId]

    override fun save(order: KitchenOrder) {
        eventPublisher.publish(order.popEvents())
        storage[order.id] = order
    }
}