package com.stringconcat.ddd.shop.crm

import com.stringconcat.ddd.shop.domain.cart.CustomerId
import com.stringconcat.ddd.shop.domain.menu.Price
import com.stringconcat.ddd.shop.domain.order.ShopOrderId
import com.stringconcat.ddd.shop.usecase.order.providers.OrderExporter
import io.github.resilience4j.bulkhead.Bulkhead
import io.github.resilience4j.bulkhead.BulkheadConfig
import io.github.resilience4j.bulkhead.BulkheadRegistry
import io.github.resilience4j.circuitbreaker.CircuitBreaker
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig
import org.slf4j.LoggerFactory
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpStatus
import org.springframework.http.client.ClientHttpResponse
import org.springframework.web.client.ResourceAccessException
import org.springframework.web.client.ResponseErrorHandler
import java.io.IOException
import java.math.BigDecimal
import java.net.SocketException
import java.net.URL
import java.time.Duration
import java.util.concurrent.TimeoutException

class CrmClient(
    baseUrl: URL,
    connectTimeout: Duration = Duration.ofMillis(CONNECT_MILLIS_TIMEOUT),
    readTimeout: Duration = Duration.ofMillis(READ_MILLIS_TIMEOUT),
    slowCallDuration: Duration = Duration.ofMillis(SLOW_CALL_MILLIS),
    maxWaitDuration: Duration = Duration.ofMillis(MAX_WAIT_MILLIS),
    maxConcurrentCalls: Int = MAX_CONCURRENT_CALLS,
    slowCallRate: Float = SLOW_CALL_RATE,
    failureRate: Float = FAILURE_RATE,
    enabled: Boolean = true
) : OrderExporter {

    private val logger = LoggerFactory.getLogger(CrmClient::class.java)
    private val enabled: () -> Boolean = { enabled }

    private val restTemplate = RestTemplateBuilder()
        .rootUri(baseUrl.toString())
        .errorHandler(EmptyErrorHandler())
        .setConnectTimeout(connectTimeout)
        .setReadTimeout(readTimeout)
        .build()

    private val circuitBreakerConfig = CircuitBreakerConfig.custom()
        .failureRateThreshold(failureRate)
        .slowCallDurationThreshold(slowCallDuration)
        .slowCallRateThreshold(slowCallRate)
        .recordExceptions(
            IOException::class.java,
            TimeoutException::class.java,
            SocketException::class.java,
            ResourceAccessException::class.java
        )
        .build()

    private val bulkheadConfig = BulkheadConfig.custom()
        .maxConcurrentCalls(maxConcurrentCalls)
        .maxWaitDuration(maxWaitDuration)
        .build()

    private val circuitBreaker = CircuitBreaker.of("CrmClientCircuitBreaker", circuitBreakerConfig)

    private val bulkhead: Bulkhead = BulkheadRegistry.of(bulkheadConfig).bulkhead("CrmClientBulkhead")

    override fun exportOrder(id: ShopOrderId, customerId: CustomerId, totalPrice: Price) {
        if (!enabled()) {
            error("Disabled!")
        }

        val request = Request(
            id = id.toLongValue(),
            customerId = customerId.value,
            totalPrice = totalPrice.toBigDecimalValue()
        )

        val response = bulkhead.executeSupplier {
            circuitBreaker.executeSupplier {
                restTemplate.postForEntity("/orders", request, Response::class.java)
            }
        }

        check(response.statusCode == HttpStatus.OK) {
            "CRM returned error code. Entity: $response"
        }

        val body = checkNotNull(response.body) {
            "CRM returned an empty body. Entity: $response"
        }

        when (body.result) {
            Result.SUCCESS -> logger.info("Order ${id.toLongValue()} exported successfully")
            Result.ALREADY_EXISTS -> logger.warn("Order ${id.toLongValue()} is already exists in CRM")
        }
    }

    data class Request(val id: Long, val customerId: String, val totalPrice: BigDecimal)
    data class Response(val result: Result)
    enum class Result { SUCCESS, ALREADY_EXISTS }

    companion object {
        const val CONNECT_MILLIS_TIMEOUT = 600L
        const val READ_MILLIS_TIMEOUT = 900L
        const val SLOW_CALL_MILLIS = 400L
        const val MAX_WAIT_MILLIS = 500L
        const val MAX_CONCURRENT_CALLS = 25
        const val SLOW_CALL_RATE = 10f
        const val FAILURE_RATE = 10f
    }
}

class EmptyErrorHandler : ResponseErrorHandler {
    override fun hasError(response: ClientHttpResponse) = false

    override fun handleError(response: ClientHttpResponse) {
        // nothing to do
    }
}