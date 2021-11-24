package com.stringconcat.ddd.shop.crm

import au.com.dius.pact.core.support.toUrl
import com.github.tomakehurst.wiremock.client.WireMock.ok
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.status
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo
import com.github.tomakehurst.wiremock.junit5.WireMockTest
import com.stringconcat.ddd.shop.domain.customerId
import com.stringconcat.ddd.shop.domain.orderId
import com.stringconcat.ddd.shop.domain.price
import io.kotest.assertions.throwables.shouldThrow
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@WireMockTest
class CrmClientIntegrationTest {

    @ParameterizedTest
    @ValueSource(ints = [201, 404, 400, 500])
    fun `exception when http status is not 200`(status: Int, wmRuntimeInfo: WireMockRuntimeInfo) {
        stubFor(post("/orders").willReturn(status(status)))
        val crmClient = CrmClient(wmRuntimeInfo.httpBaseUrl.toUrl()!!)

        shouldThrow<IllegalStateException> {
            crmClient.exportOrder(id = orderId(), customerId = customerId(), totalPrice = price())
        }
    }

    @Test
    fun `exception when body is empty`(wmRuntimeInfo: WireMockRuntimeInfo) {
        stubFor(post("/orders").willReturn(ok("")))
        val crmClient = CrmClient(wmRuntimeInfo.httpBaseUrl.toUrl()!!)

        shouldThrow<IllegalStateException> {
            crmClient.exportOrder(id = orderId(), customerId = customerId(), totalPrice = price())
        }
    }
}