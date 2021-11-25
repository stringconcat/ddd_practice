package com.stringconcat.ddd.shop.crm

import com.stringconcat.ddd.shop.domain.cart.CustomerId
import com.stringconcat.ddd.shop.domain.menu.Price
import com.stringconcat.ddd.shop.domain.order.ShopOrderId
import com.stringconcat.ddd.shop.usecase.order.providers.OrderExporter
import io.github.resilience4j.circuitbreaker.CircuitBreaker
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig
import java.io.IOException
import java.math.BigDecimal
import java.net.SocketException
import java.util.concurrent.TimeoutException
import org.slf4j.LoggerFactory
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpStatus
import org.springframework.http.client.ClientHttpResponse
import org.springframework.web.client.ResourceAccessException
import org.springframework.web.client.ResponseErrorHandler

class CrmClient(settings: CrmClientSettings) : OrderExporter {

    private val logger = LoggerFactory.getLogger(CrmClient::class.java)

    private val restTemplate = RestTemplateBuilder()
        .rootUri(settings.baseUrl.toString())
        .errorHandler(EmptyErrorHandler())
        .setConnectTimeout(settings.connectTimeout)
        .setReadTimeout(settings.readTimeout)
        .build()

    private val circuitBreakerConfig = CircuitBreakerConfig.custom()
        .failureRateThreshold(settings.failureRate)
        .slowCallDurationThreshold(settings.slowCallDuration)
        .slowCallRateThreshold(settings.slowCallRate)
        .recordExceptions(
            IOException::class.java,
            TimeoutException::class.java,
            SocketException::class.java,
            ResourceAccessException::class.java)
        .build()

    private val circuitBreaker = CircuitBreaker.of("CrmClientCircuitBreaker", circuitBreakerConfig)

    override fun exportOrder(id: ShopOrderId, customerId: CustomerId, totalPrice: Price) {
        val request = Request(id = id.toLongValue(),
            customerId = customerId.value,
            totalPrice = totalPrice.toBigDecimalValue())

        val response = circuitBreaker.executeSupplier {
            restTemplate.postForEntity("/orders", request, Response::class.java)
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
}

class EmptyErrorHandler : ResponseErrorHandler {
    override fun hasError(response: ClientHttpResponse) = false

    override fun handleError(response: ClientHttpResponse) {
        // nothing to do
    }
}