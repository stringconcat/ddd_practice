package com.stringconcat.ddd.shop.usecase.order

import com.stringconcat.ddd.order.domain.order.CustomerOrder

interface CustomerOrderPersister {
    fun save(order: CustomerOrder)
}