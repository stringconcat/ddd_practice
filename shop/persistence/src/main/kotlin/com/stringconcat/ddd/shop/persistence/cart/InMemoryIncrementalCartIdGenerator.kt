package com.stringconcat.ddd.shop.persistence.cart

import com.stringconcat.ddd.shop.domain.cart.CartId
import com.stringconcat.ddd.shop.domain.cart.CartIdGenerator
import java.util.concurrent.atomic.AtomicLong

class InMemoryIncrementalCartIdGenerator : CartIdGenerator {

    private val counter = AtomicLong(0)

    override fun generate(): CartId {
        return CartId(counter.incrementAndGet())
    }
}