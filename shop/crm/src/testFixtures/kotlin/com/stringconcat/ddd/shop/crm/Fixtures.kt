package com.stringconcat.ddd.shop.crm

import au.com.dius.pact.consumer.MockServer
import au.com.dius.pact.core.support.toUrl
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo
import com.stringconcat.ddd.shop.domain.customerId
import com.stringconcat.ddd.shop.domain.orderId
import com.stringconcat.ddd.shop.domain.price
import java.time.Duration

const val SUCCESS_BODY = """{"result": "SUCCESS" }"""

const val CORRECT_REQUEST_COUNT = 100
const val FAILURE_REQUEST_COUNT = 10
const val SLOW_REQUEST_COUNT = 10
const val THREAD_COUNT = 20
const val MAX_WAIT_DURATION = 200L

fun WireMockRuntimeInfo.buildCrmClient(
    maxConcurrentCalls: Int = 25,
    maxWaitDuration: Duration = Duration.ofMillis(MAX_WAIT_DURATION),
) =
    CrmClient(
        baseUrl = this.httpBaseUrl.toUrl()!!,
        maxConcurrentCalls = maxConcurrentCalls,
        maxWaitDuration = maxWaitDuration)

fun MockServer.buildCrmClient() =
    CrmClient(this.getUrl().toUrl()!!)

fun exportOrders(crmClient: CrmClient) = crmClient.exportOrder(
    id = orderId(),
    customerId = customerId(),
    totalPrice = price()
)