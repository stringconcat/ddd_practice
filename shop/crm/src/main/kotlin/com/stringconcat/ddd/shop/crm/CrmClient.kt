package com.stringconcat.ddd.shop.crm

import com.stringconcat.ddd.shop.domain.cart.CustomerId
import com.stringconcat.ddd.shop.domain.menu.Price
import com.stringconcat.ddd.shop.domain.order.ShopOrderId
import com.stringconcat.ddd.shop.usecase.order.providers.OrderExporter
import java.math.BigDecimal
import java.net.URL
import org.slf4j.LoggerFactory
import org.springframework.boot.web.client.RestTemplateBuilder

class CrmClient(baseUrl: URL) : OrderExporter {

    private val logger = LoggerFactory.getLogger(CrmClient::class.java)

    private val restTemplate = RestTemplateBuilder()
        .rootUri(baseUrl.toString()).build()

    override fun exportOrder(id: ShopOrderId, customerId: CustomerId, totalPrice: Price) {
        val request = Request(id = id.value,
            customerId = customerId.value,
            totalPrice = totalPrice.value)

        val response = restTemplate.postForEntity("/orders", request, Response::class.java)
        val body = checkNotNull(response.body) {
            "CRM returned an empty body"
        }

        when (body.result) {
            Result.SUCCESS -> logger.info("Order ${id.value} exported successfully")
            Result.ALREADY_EXISTS -> logger.warn("Order ${id.value} is already exists in CRM")
        }
    }

    data class Request(val id: Long, val customerId: String, val totalPrice: BigDecimal)
    data class Response(val result: Result)
    enum class Result { SUCCESS, ALREADY_EXISTS }
}