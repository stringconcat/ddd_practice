package com.stringconcat.dev.course.app.configuration.shop

import com.stringconcat.shop.payment.SimplePaymentUrlProvider
import java.net.URL
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ShopPaymentConfiguration {
    @Bean
    fun paymentUrlProvider() = SimplePaymentUrlProvider(URL("http://localhost:8080"))
}