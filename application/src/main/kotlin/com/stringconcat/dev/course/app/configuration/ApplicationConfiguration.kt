package com.stringconcat.dev.course.app.configuration

import com.stringconcat.dev.course.app.event.EventPublisherImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(
    CustomerOrderContextConfiguration::class,
    KitchenContextConfiguration::class,
    ContextsIntegration::class,
    TelnetConfiguration::class
)
class ApplicationConfiguration {

    @Bean
    fun eventPublisher() = EventPublisherImpl()
}