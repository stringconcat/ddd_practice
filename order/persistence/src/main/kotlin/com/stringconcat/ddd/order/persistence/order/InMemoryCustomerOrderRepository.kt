package com.stringconcat.ddd.order.persistence.order

import com.stringconcat.ddd.common.types.base.EventPublisher
import com.stringconcat.ddd.order.domain.cart.CustomerId
import com.stringconcat.ddd.order.domain.order.CustomerOrder
import com.stringconcat.ddd.order.domain.order.CustomerOrderId
import com.stringconcat.ddd.order.usecase.order.CustomerOrderExtractor
import com.stringconcat.ddd.order.usecase.order.CustomerOrderPersister

class InMemoryCustomerOrderRepository(private val eventPublisher: EventPublisher) :
    CustomerOrderExtractor,
    CustomerOrderPersister {

    internal val storage = LinkedHashMap<CustomerOrderId, CustomerOrder>()

    override fun getById(orderId: CustomerOrderId) = storage[orderId]

    override fun getLastOrder(customerId: CustomerId) =
        storage.filter { it.component2().customerId == customerId }.values.lastOrNull()

    override fun save(order: CustomerOrder) {
        eventPublisher.publish(order.popEvents())
        storage[order.id] = order
    }
}