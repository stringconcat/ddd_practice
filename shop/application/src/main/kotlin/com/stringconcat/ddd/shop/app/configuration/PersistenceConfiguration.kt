package com.stringconcat.ddd.shop.app.configuration

import com.stringconcat.ddd.common.events.DomainEventPublisher
import com.stringconcat.ddd.shop.persistence.cart.InMemoryCartRepository
import com.stringconcat.ddd.shop.persistence.cart.InMemoryIncrementalCartIdGenerator
import com.stringconcat.ddd.shop.persistence.order.InMemoryIncrementalShopOrderIdGenerator
import com.stringconcat.ddd.shop.persistence.order.InMemoryShopOrderRepository
import com.stringconcat.ddd.shop.persistence.postgresql.PostgresMealIdGenerator
import com.stringconcat.ddd.shop.persistence.postgresql.PostgresMealRepository
import javax.sql.DataSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PersistenceConfiguration {
    @Bean
    fun cartRepository(eventPublisher: DomainEventPublisher) = InMemoryCartRepository(eventPublisher)

    @Bean
    fun mealRepository(dataSource: DataSource, eventPublisher: DomainEventPublisher) =
        PostgresMealRepository(dataSource, eventPublisher)

    @Bean
    fun shopOrderRepository(eventPublisher: DomainEventPublisher) = InMemoryShopOrderRepository(eventPublisher)

    @Bean
    fun cartIdGenerator() = InMemoryIncrementalCartIdGenerator()

    @Bean
    fun mealIdGenerator(dataSource: DataSource) = PostgresMealIdGenerator(dataSource)

    @Bean
    fun shopOrderIdGenerator() = InMemoryIncrementalShopOrderIdGenerator()
}