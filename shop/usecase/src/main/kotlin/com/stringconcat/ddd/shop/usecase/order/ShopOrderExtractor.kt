package com.stringconcat.ddd.shop.usecase.order

import com.stringconcat.ddd.shop.domain.cart.CustomerId
import com.stringconcat.ddd.shop.domain.order.ShopOrder
import com.stringconcat.ddd.shop.domain.order.ShopOrderId

interface ShopOrderExtractor {
    fun getById(orderId: ShopOrderId): ShopOrder?

    fun getLastOrder(forCustomer: CustomerId): ShopOrder?

    fun getAll(): List<ShopOrder>
}