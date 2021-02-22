package com.stringconcat.dev.course.app.configuration

import com.stringconcat.ddd.common.types.base.EventPublisher
import com.stringconcat.ddd.kitchen.persistence.order.InMemoryKitchenOrderRepository
import com.stringconcat.ddd.kitchen.usecase.order.CookOrderUseCase
import com.stringconcat.ddd.kitchen.usecase.order.CreateOrderHandler
import com.stringconcat.ddd.kitchen.usecase.order.KitchenOrderExtractor
import com.stringconcat.ddd.kitchen.usecase.order.KitchenOrderPersister
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@Suppress("TooManyFunctions")
class KitchenContextConfiguration {

    @Bean
    fun kitchenOrderRepository(eventPublisher: EventPublisher) = InMemoryKitchenOrderRepository(eventPublisher)

    @Bean
    fun cookOrderUseCase(
        kitchenOrderExtractor: KitchenOrderExtractor,
        kitchenOrderPersister: KitchenOrderPersister
    ) = CookOrderUseCase(kitchenOrderExtractor, kitchenOrderPersister)

    @Bean
    fun createOrderHandler(
        kitchenOrderExtractor: KitchenOrderExtractor,
        kitchenOrderPersister: KitchenOrderPersister
    ) = CreateOrderHandler(kitchenOrderExtractor, kitchenOrderPersister)
}