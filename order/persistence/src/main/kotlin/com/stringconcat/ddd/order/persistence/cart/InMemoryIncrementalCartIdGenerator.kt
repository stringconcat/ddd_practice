package com.stringconcat.ddd.order.persistence.cart

import com.stringconcat.ddd.order.domain.cart.CartId
import com.stringconcat.ddd.order.domain.cart.CartIdGenerator
import java.util.concurrent.atomic.AtomicLong

class InMemoryIncrementalCartIdGenerator : CartIdGenerator {

    private val counter = AtomicLong(0)

    override fun generate(): CartId {
        return CartId(counter.incrementAndGet())
    }
}