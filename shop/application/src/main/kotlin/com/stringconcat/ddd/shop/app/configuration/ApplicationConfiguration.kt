package com.stringconcat.ddd.shop.app.configuration

import com.stringconcat.ddd.shop.app.controllers.IndexEndpoint
import com.stringconcat.ddd.shop.app.controllers.PaymentController
import com.stringconcat.ddd.shop.app.event.EventPublisherImpl
import com.stringconcat.ddd.shop.usecase.order.PayOrder
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(
    IntegrationConfiguration::class,
    PersistenceConfiguration::class,
    RestConfiguration::class,
    UseCaseConfiguration::class,
    ContextsIntegration::class,
    TelnetConfiguration::class,
    TelnetServerConfiguration::class,
    MvcConfiguration::class,
    MessagingConfiguration::class
)
@EnableAutoConfiguration
class ApplicationConfiguration {

    @Bean
    fun eventPublisher() = EventPublisherImpl()

    @Bean
    fun indexEndpoint() = IndexEndpoint()

    @Bean
    fun paymentController(payOrder: PayOrder) = PaymentController(payOrder)
}