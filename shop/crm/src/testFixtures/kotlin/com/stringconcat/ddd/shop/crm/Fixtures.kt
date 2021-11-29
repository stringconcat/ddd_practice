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
const val THREAD_COUNT = 500

fun WireMockRuntimeInfo.buildCrmClient() =
    CrmClient(this.httpBaseUrl.toUrl()!!)

fun MockServer.buildCrmClient() =
    CrmClient(this.getUrl().toUrl()!!)

fun exportOrders(crmClient: CrmClient) = crmClient.exportOrder(
    id = orderId(),
    customerId = customerId(),
    totalPrice = price()
)