package com.stringconcat.shop.payment

import com.stringconcat.ddd.shop.domain.menu.Price
import com.stringconcat.ddd.shop.domain.order.ShopOrderId
import com.stringconcat.ddd.shop.usecase.order.providers.PaymentUrlProvider
import java.net.URL

class SimplePaymentUrlProvider(private val currentUrl: URL) : PaymentUrlProvider {
    override fun provideUrl(orderId: ShopOrderId, price: Price): URL {
        return URL("$currentUrl/payment?orderId=${orderId.toLongValue()}&price=${price.toBigDecimalValue()}")
    }
}