package com.stringconcat.shop.payment

import com.stringconcat.ddd.shop.domain.orderId
import com.stringconcat.ddd.shop.domain.price
import io.kotest.matchers.shouldBe
import java.net.URL
import org.junit.jupiter.api.Test

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