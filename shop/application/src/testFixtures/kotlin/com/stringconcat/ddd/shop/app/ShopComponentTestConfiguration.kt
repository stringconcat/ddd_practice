package com.stringconcat.ddd.shop.app

import com.stringconcat.ddd.common.events.DomainEventPublisher
import com.stringconcat.ddd.shop.app.configuration.MvcConfiguration
import com.stringconcat.ddd.shop.app.configuration.RestConfiguration
import com.stringconcat.ddd.shop.app.configuration.TelnetConfiguration
import com.stringconcat.ddd.shop.app.configuration.TelnetServerConfiguration
import com.stringconcat.ddd.shop.app.configuration.UseCaseConfiguration
import com.stringconcat.ddd.shop.app.event.EventPublisherImpl
import com.stringconcat.ddd.shop.persistence.cart.InMemoryCartRepository
import com.stringconcat.ddd.shop.persistence.cart.InMemoryIncrementalCartIdGenerator
import com.stringconcat.ddd.shop.persistence.menu.InMemoryIncrementalMealIdGenerator
import com.stringconcat.ddd.shop.persistence.menu.InMemoryMealRepository
import com.stringconcat.ddd.shop.persistence.order.InMemoryIncrementalShopOrderIdGenerator
import com.stringconcat.ddd.shop.persistence.order.InMemoryShopOrderRepository
import com.stringconcat.ddd.shop.usecase.MockOrderExporter
import com.stringconcat.shop.payment.SimplePaymentUrlProvider
import java.net.URL
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(
    TelnetConfiguration::class,
    RestConfiguration::class,
    UseCaseConfiguration::class,
    TelnetServerConfiguration::class,
    MvcConfiguration::class)
class ShopComponentTestConfiguration {

    @Bean
    fun simplePaymentProvider() = SimplePaymentUrlProvider(URL("http://localhost:8081"))

    @Bean
    fun eventPublisher() = EventPublisherImpl()

    @Bean
    fun mockOrderExporter() = MockOrderExporter()

    @Bean
    fun mealRepository(eventPublisher: DomainEventPublisher) =
        InMemoryMealRepository(eventPublisher)

    @Bean
    fun mealIdGenerator() = InMemoryIncrementalMealIdGenerator()

    @Bean
    fun cartRepository(eventPublisher: DomainEventPublisher) = InMemoryCartRepository(eventPublisher)

    @Bean
    fun shopOrderRepository(eventPublisher: DomainEventPublisher) = InMemoryShopOrderRepository(eventPublisher)

    @Bean
    fun cartIdGenerator() = InMemoryIncrementalCartIdGenerator()

    @Bean
    fun shopOrderIdGenerator() = InMemoryIncrementalShopOrderIdGenerator()

    @Bean
    fun errorController() = ErrorController()
}