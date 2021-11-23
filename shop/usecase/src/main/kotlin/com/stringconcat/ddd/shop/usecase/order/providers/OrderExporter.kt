package com.stringconcat.ddd.shop.usecase.order.providers

import com.stringconcat.ddd.shop.domain.cart.CustomerId
import com.stringconcat.ddd.shop.domain.menu.Price
import com.stringconcat.ddd.shop.domain.order.ShopOrderId

interface OrderExporter {
    fun exportOrder(id: ShopOrderId, customerId: CustomerId, totalPrice: Price)
}