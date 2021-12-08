package com.stringconcat.ddd.shop.app.configuration

import com.stringconcat.ddd.shop.crm.CrmClient
import com.stringconcat.shop.payment.SimplePaymentUrlProvider
import java.net.URL
import java.time.Duration
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class IntegrationConfiguration(
    @Value("\${crm.url}") val url: URL,
) {
    @Bean
    fun paymentUrlProvider() = SimplePaymentUrlProvider(URL("http://localhost:8080"))

    @Bean
    @Suppress("MagicNumber") // уберем чуть позже
    fun crmClient(): CrmClient {
        return CrmClient(baseUrl = url,
            connectTimeout = Duration.ofSeconds(1),
            readTimeout = Duration.ofSeconds(1),
            failureRate = 50.0f,
            slowCallRate = 50.0f,
            slowCallDuration = Duration.ofSeconds(1))
    }
}