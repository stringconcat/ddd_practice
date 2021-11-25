package com.stringconcat.ddd.shop.crm

import com.github.tomakehurst.wiremock.client.WireMock.ok
import com.github.tomakehurst.wiremock.client.WireMock.okForContentType
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.status
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.http.Fault
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo
import com.github.tomakehurst.wiremock.junit5.WireMockTest
import com.stringconcat.ddd.shop.domain.customerId
import com.stringconcat.ddd.shop.domain.orderId
import com.stringconcat.ddd.shop.domain.price
import io.github.resilience4j.circuitbreaker.CallNotPermittedException
import io.kotest.assertions.throwables.shouldThrow
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.http.MediaType

@WireMockTest
class CrmClientIntegrationTest {

    @ParameterizedTest
    @ValueSource(ints = [201, 404, 400, 500])
    fun `exception when http status is not 200`(status: Int, wmRuntimeInfo: WireMockRuntimeInfo) {
        stubFor(post("/orders")
            .willReturn(status(status))) // включить сюда валидное тело ответа

        val crmClient = wmRuntimeInfo.buildCrmClient()

        shouldThrow<IllegalStateException> {
            crmClient.exportOrder(id = orderId(), customerId = customerId(), totalPrice = price())
        }
    }

    @Test
    fun `exception when body is empty`(wmRuntimeInfo: WireMockRuntimeInfo) {
        stubFor(post("/orders").willReturn(ok("")))

        val crmClient = wmRuntimeInfo.buildCrmClient()

        shouldThrow<IllegalStateException> {
            crmClient.exportOrder(id = orderId(), customerId = customerId(), totalPrice = price())
        }
    }

    @Test
    fun `circuit breaker enables after failures`(wmRuntimeInfo: WireMockRuntimeInfo) {
        val crmClient = wmRuntimeInfo.buildCrmClient() // причесать

        stubFor(post("/orders")
            .willReturn(okForContentType(MediaType.APPLICATION_JSON_VALUE, """{"result": "SUCCESS" }""")))

        repeat(100) {
            crmClient.exportOrder(id = orderId(), customerId = customerId(), totalPrice = price())
        }

        stubFor(post("/orders")
            .willReturn(ok("").withFault(Fault.CONNECTION_RESET_BY_PEER)))

        repeat(10) {
            try {
                crmClient.exportOrder(id = orderId(), customerId = customerId(), totalPrice = price())
            } catch (ignored: Exception) {
            }
        }

        shouldThrow<CallNotPermittedException> {
            crmClient.exportOrder(id = orderId(), customerId = customerId(), totalPrice = price())
        }
    }
    // проверить долгие запорсы
}