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
import io.kotest.matchers.collections.shouldNotBeEmpty
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.http.MediaType
import java.time.Duration
import java.util.concurrent.CyclicBarrier

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
                .willReturn(ok(SUCCESS_BODY).withFixedDelay(500))
        )

        repeat(SLOW_REQUEST_COUNT) {
            try {
                exportOrders(crmClient)
            } catch (ignored: Exception) {
            }
        }

        shouldThrow<CallNotPermittedException> { exportOrders(crmClient) }
    }

    @Test
    fun `bulkhead enables after unlimited execution`(wmRuntimeInfo: WireMockRuntimeInfo) {
        val crmClient = wmRuntimeInfo
            .buildCrmClient(
                maxConcurrentCalls = THREAD_COUNT / 2,
                maxWaitDuration = Duration.ofMillis(0)
            )

        stubFor(
            post("/orders")
                .willReturn(okForContentType(MediaType.APPLICATION_JSON_VALUE, SUCCESS_BODY))
        )
        val barrier = CyclicBarrier(THREAD_COUNT)

        val workers = List(THREAD_COUNT) {
            Worker(crmClient, barrier)
        }

        workers.forEach { it.start() }
        workers.forEach { it.join() }

        workers.filter { it.bulkheadExceptionHasBeenThrown }.shouldNotBeEmpty()
    }

    @Test
    fun `client shutdown after disabling`(wmRuntimeInfo: WireMockRuntimeInfo) {
        val crmClient = wmRuntimeInfo.buildDisabledCrmClient()

        shouldThrow<IllegalStateException> { exportOrders(crmClient) }
    }

    internal class Worker(
        private val crmClient: CrmClient,
        private val cyclicBarrier: CyclicBarrier,
    ) : Thread() {

        var bulkheadExceptionHasBeenThrown = false
            private set

        override fun run() {
            cyclicBarrier.await()
            try {
                exportOrders(crmClient)
            } catch (ignored: Exception) {
                bulkheadExceptionHasBeenThrown = true
            }
        }
    }
}