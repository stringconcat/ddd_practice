package com.stringconcat.ddd.shop.persistence.order

import com.stringconcat.ddd.shop.domain.order.CustomerOrderId
import com.stringconcat.ddd.shop.domain.order.CustomerOrderIdGenerator
import java.util.concurrent.atomic.AtomicLong

class InMemoryIncrementalCustomerOrderIdGenerator : CustomerOrderIdGenerator {

    private val counter = AtomicLong(0)

    override fun generate(): CustomerOrderId {
        return CustomerOrderId(counter.incrementAndGet())
    }
}