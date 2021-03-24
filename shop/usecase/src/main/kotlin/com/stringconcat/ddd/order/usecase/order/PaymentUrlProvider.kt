package com.stringconcat.ddd.order.usecase.order

import com.stringconcat.ddd.order.domain.menu.Price
import com.stringconcat.ddd.order.domain.order.CustomerOrderId
import java.net.URL

interface PaymentUrlProvider {
    fun provideUrl(orderId: CustomerOrderId, price: Price): URL
}