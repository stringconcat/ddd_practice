package com.stringconcat.ddd.kitchen.persistence.order

import com.stringconcat.ddd.common.types.base.DomainEvent
import com.stringconcat.ddd.common.types.base.EventPublisher
import com.stringconcat.ddd.kitchen.domain.order.KitchenOrder
import com.stringconcat.ddd.kitchen.domain.order.KitchenOrderId
import com.stringconcat.ddd.kitchen.domain.order.order
import com.stringconcat.ddd.kitchen.domain.order.orderId

fun orderWithEvents(id: KitchenOrderId = orderId()): KitchenOrder {
    return order(id, false).apply {
        cook()
    }
}

class TestEventPublisher : EventPublisher {
    val storage = ArrayList<DomainEvent>()
    override fun publish(events: Collection<DomainEvent>) {
        storage.addAll(events)
    }
}
