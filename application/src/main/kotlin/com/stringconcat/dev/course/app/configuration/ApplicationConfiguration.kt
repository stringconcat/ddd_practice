package com.stringconcat.dev.course.app.configuration

import com.stringconcat.ddd.common.types.base.DomainEvent
import com.stringconcat.dev.course.app.event.DomainEventListener
import com.stringconcat.dev.course.app.event.EventPublisherImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(
    CustomerOrderContextConfiguration::class,
    KitchenContextConfiguration::class
)
class ApplicationConfiguration {

    @Bean
    fun eventPublisher(listeners: List<DomainEventListener<out DomainEvent>>) = EventPublisherImpl(listeners)
}