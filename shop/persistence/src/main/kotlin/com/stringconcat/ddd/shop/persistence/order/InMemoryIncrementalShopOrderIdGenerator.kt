package com.stringconcat.ddd.shop.persistence.order

import com.stringconcat.ddd.shop.domain.order.ShopOrderId
import com.stringconcat.ddd.shop.domain.order.ShopOrderIdGenerator
import java.util.concurrent.atomic.AtomicLong

class InMemoryIncrementalShopOrderIdGenerator : ShopOrderIdGenerator {

    private val counter = AtomicLong(0)

    override fun generate(): ShopOrderId {
        return ShopOrderId(counter.incrementAndGet())
    }
}