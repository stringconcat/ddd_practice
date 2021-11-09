package com.stringconcat.ddd.shop.usecase.order.access

import com.stringconcat.ddd.shop.domain.order.ShopOrder

interface ShopOrderPersister {
    fun save(order: ShopOrder)
}