package com.stringconcat.ddd.shop.crm

import au.com.dius.pact.consumer.MockServer
import au.com.dius.pact.core.support.toUrl
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo
import com.stringconcat.ddd.shop.domain.customerId
import com.stringconcat.ddd.shop.domain.orderId
import com.stringconcat.ddd.shop.domain.price
import java.net.URL
import java.time.Duration

const val SUCCESS_BODY = """{"result": "SUCCESS" }"""

const val CORRECT_REQUEST_COUNT = 100
const val FAILURE_REQUEST_COUNT = 10
const val SLOW_REQUEST_COUNT = 10

fun WireMockRuntimeInfo.buildCrmClient() =
    CrmClient(crmClientSettings(baseURL = this.httpBaseUrl.toUrl()!!))

fun MockServer.buildCrmClient() =
    CrmClient(crmClientSettings(baseURL = this.getUrl().toUrl()!!))

fun exportOrders(crmClient: CrmClient) = crmClient.exportOrder(
    id = orderId(),
    customerId = customerId(),
    totalPrice = price()
)

fun crmClientSettings(baseURL: URL) =
    CrmClientSettings(
        baseUrl = baseURL,
        connectTimeout = Duration.ofMillis(600),
        readTimeout = Duration.ofMillis(900),
        slowCallDuration = Duration.ofMillis(400),
        slowCallRate = 10f,
        failureRate = 10f
    )