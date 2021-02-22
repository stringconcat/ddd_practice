package com.stringconcat.integration.payment

import com.stringconcat.ddd.order.domain.menu.Price
import com.stringconcat.ddd.order.domain.order.CustomerOrderId
import com.stringconcat.ddd.order.usecase.order.PaymentUrlProvider
import java.net.URL

class SimplePaymentUrlProvider : PaymentUrlProvider {
    override fun provideUrl(orderId: CustomerOrderId, price: Price): URL {
        return URL("http://localhost/payment?orderId=${orderId.value}&price=${price.value}")
    }
}