package com.stringconcat.ddd.kitchen.app.configuration

import com.stringconcat.ddd.common.events.DomainEventPublisher
import com.stringconcat.ddd.common.types.base.DomainEvent
import com.stringconcat.ddd.kitchen.app.controllers.IndexEndpoint
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(
    KitchenContextConfiguration::class,
    SwaggerConfiguration::class,
    MvcConfiguration::class
)
@EnableAutoConfiguration
class ApplicationConfiguration {

    @Bean
    fun indexController() = IndexEndpoint()

    @Bean
    fun domainEventPublisher() = object : DomainEventPublisher {

        private val logger = LoggerFactory.getLogger("EventPublisher")

        override fun publish(events: Collection<DomainEvent>) {
            events.forEach { logger.info("Processing event: $it") }
        }
    }
}