package com.stringconcat.shop.payment

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.net.URL

class SimplePaymentUrlProviderTest {

    @Test
    fun `provide url`() {
        val provider = SimplePaymentUrlProvider(URL("http://localhost"))
        val orderId = orderId()
        val price = price()
        val url = provider.provideUrl(orderId, price)
        url shouldBe URL("http://localhost/payment?orderId=${orderId.value}&price=${price.value}")
    }
}