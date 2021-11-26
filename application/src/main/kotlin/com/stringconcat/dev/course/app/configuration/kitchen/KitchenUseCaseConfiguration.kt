package com.stringconcat.dev.course.app.configuration.kitchen

import com.stringconcat.ddd.kitchen.usecase.order.access.KitchenOrderExtractor
import com.stringconcat.ddd.kitchen.usecase.order.access.KitchenOrderPersister
import com.stringconcat.ddd.kitchen.usecase.order.scenarios.CookOrderUseCase
import com.stringconcat.ddd.kitchen.usecase.order.scenarios.CreateOrderHandler
import com.stringconcat.ddd.kitchen.usecase.order.scenarios.GetOrderByIdUseCase
import com.stringconcat.ddd.kitchen.usecase.order.scenarios.GetOrdersUseCase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class KitchenUseCaseConfiguration {

    @Bean
    fun kitchenGetOrderById(kitchenOrderExtractor: KitchenOrderExtractor) = GetOrderByIdUseCase(kitchenOrderExtractor)

    @Bean
    fun kitchenGetOrdersUseCase(kitchenOrderExtractor: KitchenOrderExtractor) = GetOrdersUseCase(kitchenOrderExtractor)

    @Bean
    fun cookOrderUseCase(
        kitchenOrderExtractor: KitchenOrderExtractor,
        kitchenOrderPersister: KitchenOrderPersister,
    ) = CookOrderUseCase(kitchenOrderExtractor, kitchenOrderPersister)

    @Bean
    fun createOrderHandler(
        kitchenOrderExtractor: KitchenOrderExtractor,
        kitchenOrderPersister: KitchenOrderPersister,
    ) = CreateOrderHandler(kitchenOrderExtractor, kitchenOrderPersister)
}