package com.stringconcat.ddd.shop.persistence.order

import com.stringconcat.ddd.common.types.base.EventPublisher
import com.stringconcat.ddd.shop.domain.cart.CustomerId
import com.stringconcat.ddd.shop.domain.shop.CustomerOrder
import com.stringconcat.ddd.shop.domain.shop.CustomerOrderId
import com.stringconcat.ddd.shop.usecase.shop.CustomerOrderExtractor
import com.stringconcat.ddd.shop.usecase.shop.CustomerOrderPersister

class InMemoryCustomerOrderRepository(private val eventPublisher: EventPublisher) :
    CustomerOrderExtractor,
    CustomerOrderPersister {

    internal val storage = LinkedHashMap<CustomerOrderId, CustomerOrder>()

    override fun getById(orderId: CustomerOrderId) = storage[orderId]

    override fun getLastOrder(forCustomer: CustomerId) =
        storage.filter { it.component2().forCustomer == forCustomer }.values.lastOrNull()

    override fun save(order: CustomerOrder) {
        eventPublisher.publish(order.popEvents())
        storage[order.id] = order
    }

    override fun getAll(): List<CustomerOrder> = storage.values.toList()
}