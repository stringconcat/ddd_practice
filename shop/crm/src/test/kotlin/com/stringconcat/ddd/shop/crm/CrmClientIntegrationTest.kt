package com.stringconcat.ddd.shop.crm

import com.github.tomakehurst.wiremock.client.WireMock.ok
import com.github.tomakehurst.wiremock.client.WireMock.okForContentType
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.status
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.http.Fault
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo
import com.github.tomakehurst.wiremock.junit5.WireMockTest
import com.google.common.net.HttpHeaders
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
        stubFor(
            post("/orders")
                .willReturn(
                    status(status).withBody(SUCCESS_BODY).withHeader(
                        HttpHeaders.CONTENT_TYPE,
                        MediaType.APPLICATION_JSON_VALUE
                    )
                )
        )

        val crmClient = wmRuntimeInfo.buildCrmClient()

        shouldThrow<IllegalStateException> { exportOrders(crmClient) }
    }

    @Test
    fun `exception when body is empty`(wmRuntimeInfo: WireMockRuntimeInfo) {
        stubFor(post("/orders").willReturn(ok("")))

        val crmClient = wmRuntimeInfo.buildCrmClient()

        shouldThrow<IllegalStateException> { exportOrders(crmClient) }
    }

    @Test
    fun `circuit breaker enables after failures`(wmRuntimeInfo: WireMockRuntimeInfo) {
        val crmClient = wmRuntimeInfo.buildCrmClient()

        stubFor(
            post("/orders")
                .willReturn(okForContentType(MediaType.APPLICATION_JSON_VALUE, SUCCESS_BODY))
        )

        repeat(CORRECT_REQUEST_COUNT) { exportOrders(crmClient) }

        stubFor(
            post("/orders")
                .willReturn(ok("").withFault(Fault.CONNECTION_RESET_BY_PEER))
        )

        repeat(FAILURE_REQUEST_COUNT) {
            try {
                exportOrders(crmClient)
            } catch (ignored: Exception) {
            }
        }

        shouldThrow<CallNotPermittedException> { exportOrders(crmClient) }
    }

    @Test
    fun `circuit breaker enables after slow call`(wmRuntimeInfo: WireMockRuntimeInfo) {
        val crmClient = wmRuntimeInfo.buildCrmClient()

        stubFor(
            post("/orders")
                .willReturn(okForContentType(MediaType.APPLICATION_JSON_VALUE, SUCCESS_BODY))
        )

        repeat(CORRECT_REQUEST_COUNT) { exportOrders(crmClient) }

        stubFor(
            post("/orders")
                .willReturn(ok("").withFixedDelay(500))
        )

        repeat(SLOW_REQUEST_COUNT) {
            try {
                exportOrders(crmClient)
            } catch (ignored: Exception) {
            }
        }

        shouldThrow<CallNotPermittedException> { exportOrders(crmClient) }
    }
}