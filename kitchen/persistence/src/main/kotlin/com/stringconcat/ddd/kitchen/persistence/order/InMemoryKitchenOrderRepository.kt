package com.stringconcat.ddd.kitchen.persistence.order

import com.stringconcat.ddd.common.events.DomainEventPublisher
import com.stringconcat.ddd.kitchen.domain.order.KitchenOrder
import com.stringconcat.ddd.kitchen.domain.order.KitchenOrderId
import com.stringconcat.ddd.kitchen.usecase.order.scenarios.KitchenOrderExtractor
import com.stringconcat.ddd.kitchen.usecase.order.scenarios.KitchenOrderPersister

class InMemoryKitchenOrderRepository(private val eventPublisher: DomainEventPublisher) :
    KitchenOrderExtractor,
    KitchenOrderPersister {

    internal val storage = LinkedHashMap<KitchenOrderId, KitchenOrder>()

    override fun getById(orderId: KitchenOrderId) = storage[orderId]

    override fun getAll() = storage.values.toList()

    override fun save(order: KitchenOrder) {
        eventPublisher.publish(order.popEvents())
        storage[order.id] = order
    }
}