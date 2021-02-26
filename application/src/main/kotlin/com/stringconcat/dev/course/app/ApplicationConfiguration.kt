package com.stringconcat.dev.course.app

import com.stringconcat.dev.course.app.integration.ContextsIntegration
import com.stringconcat.dev.course.app.event.EventPublisherImpl
import com.stringconcat.dev.course.app.kitchen.KitchenContextConfiguration
import com.stringconcat.dev.course.app.order.configuration.CustomerOrderContextConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(
    CustomerOrderContextConfiguration::class,
    KitchenContextConfiguration::class,
    ContextsIntegration::class
)
class ApplicationConfiguration {

    @Bean
    fun eventPublisher() = EventPublisherImpl()
}