package com.stringconcat.dev.course.app.configuration.shop

import com.stringconcat.ddd.common.events.DomainEventPublisher
import com.stringconcat.ddd.shop.persistence.cart.InMemoryCartRepository
import com.stringconcat.ddd.shop.persistence.cart.InMemoryIncrementalCartIdGenerator
import com.stringconcat.ddd.shop.persistence.menu.InMemoryIncrementalMealIdGenerator
import com.stringconcat.ddd.shop.persistence.menu.InMemoryMealRepository
import com.stringconcat.ddd.shop.persistence.order.InMemoryIncrementalShopOrderIdGenerator
import com.stringconcat.ddd.shop.persistence.order.InMemoryShopOrderRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ShopPersistenceConfiguration {
    @Bean
    fun cartRepository(eventPublisher: DomainEventPublisher) = InMemoryCartRepository(eventPublisher)

    @Bean
    fun mealRepository(eventPublisher: DomainEventPublisher) = InMemoryMealRepository(eventPublisher)

    @Bean
    fun shopOrderRepository(eventPublisher: DomainEventPublisher) = InMemoryShopOrderRepository(eventPublisher)

    @Bean
    fun cartIdGenerator() = InMemoryIncrementalCartIdGenerator()

    @Bean
    fun mealIdGenerator() = InMemoryIncrementalMealIdGenerator()

    @Bean
    fun shopOrderIdGenerator() = InMemoryIncrementalShopOrderIdGenerator()
}