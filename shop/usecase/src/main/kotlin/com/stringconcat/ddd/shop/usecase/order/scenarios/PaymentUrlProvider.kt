package com.stringconcat.ddd.shop.usecase.order.scenarios

import com.stringconcat.ddd.shop.domain.menu.Price
import com.stringconcat.ddd.shop.domain.order.ShopOrderId
import java.net.URL

interface PaymentUrlProvider {
    fun provideUrl(orderId: ShopOrderId, price: Price): URL
}