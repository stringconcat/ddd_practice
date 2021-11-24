package com.stringconcat.ddd.shop.crm

import java.net.URL
import java.time.Duration

data class CrmClientSettings(
    val baseUrl: URL,
    val connectTimeout: Duration,
    val readTimeout: Duration,
    val failureRate: Float,
    val slowCallDuration: Duration,
    val slowCallRate: Float
)
