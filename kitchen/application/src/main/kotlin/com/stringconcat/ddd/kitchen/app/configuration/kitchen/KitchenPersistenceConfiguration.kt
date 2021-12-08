package com.stringconcat.ddd.kitchen.app.configuration.kitchen

import com.stringconcat.ddd.common.events.DomainEventPublisher
import com.stringconcat.ddd.kitchen.persistence.order.InMemoryKitchenOrderRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class KitchenPersistenceConfiguration {

    @Bean
    fun kitchenOrderRepository(eventPublisher: DomainEventPublisher) = InMemoryKitchenOrderRepository(eventPublisher)
}