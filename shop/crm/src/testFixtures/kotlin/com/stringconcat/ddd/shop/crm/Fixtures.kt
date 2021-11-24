package com.stringconcat.ddd.shop.crm

import au.com.dius.pact.consumer.MockServer
import au.com.dius.pact.core.support.toUrl
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo
import java.net.URL
import java.time.Duration

fun WireMockRuntimeInfo.buildCrmClient() =
    CrmClient(crmClientSettings(baseURL = this.httpBaseUrl.toUrl()!!))

fun MockServer.buildCrmClient() =
    CrmClient(crmClientSettings(baseURL = this.getUrl().toUrl()!!))

fun crmClientSettings(baseURL: URL) =
    CrmClientSettings(
        baseUrl = baseURL,
        connectTimeout = Duration.ofMillis(200),
        readTimeout = Duration.ofMillis(500),
        slowCallDuration = Duration.ofMillis(400),
        slowCallRate = 0.5f,
        failureRate = 10f
    )