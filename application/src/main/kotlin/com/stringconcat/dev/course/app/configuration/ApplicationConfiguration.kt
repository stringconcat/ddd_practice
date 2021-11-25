package com.stringconcat.dev.course.app.configuration

import com.stringconcat.ddd.shop.usecase.order.PayOrder
import com.stringconcat.dev.course.app.controllers.IndexEndpoint
import com.stringconcat.dev.course.app.controllers.payment.PaymentController
import com.stringconcat.dev.course.app.event.EventPublisherImpl
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(
    ShopContextConfiguration::class,
    KitchenContextConfiguration::class,
    ContextsIntegration::class,
    TelnetConfiguration::class,
    SwaggerConfiguration::class,
    MvcConfiguration::class
)
@EnableAutoConfiguration
class ApplicationConfiguration {

    @Bean
    fun eventPublisher() = EventPublisherImpl()

    @Bean
    fun indexController() = IndexEndpoint()

    @Bean
    fun paymentController(payOrder: PayOrder) = PaymentController(payOrder)
}