package com.stringconcat.dev.course.app.configuration

import com.stringconcat.ddd.common.events.DomainEventPublisher
import com.stringconcat.ddd.kitchen.persistence.order.InMemoryKitchenOrderRepository
import com.stringconcat.ddd.kitchen.usecase.order.CookOrder
import com.stringconcat.ddd.kitchen.usecase.order.GetOrders
import com.stringconcat.ddd.kitchen.usecase.order.access.KitchenOrderExtractor
import com.stringconcat.ddd.kitchen.usecase.order.access.KitchenOrderPersister
import com.stringconcat.ddd.kitchen.usecase.order.scenarios.CookOrderUseCase
import com.stringconcat.ddd.kitchen.usecase.order.scenarios.CreateOrderHandler
import com.stringconcat.ddd.kitchen.usecase.order.scenarios.GetOrdersUseCase
import com.stringconcat.ddd.kitchen.web.order.KitchenOrderController
import com.stringconcat.ddd.kitchen.rest.order.CookOrderEndpoint
import com.stringconcat.ddd.kitchen.rest.order.GetOrderByIdEndpoint
import com.stringconcat.ddd.kitchen.usecase.order.GetOrderById
import com.stringconcat.ddd.kitchen.usecase.order.scenarios.GetOrderByIdUseCase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@Suppress("TooManyFunctions")
class KitchenContextConfiguration {

    @Bean
    fun kitchenOrderRepository(eventPublisher: DomainEventPublisher) = InMemoryKitchenOrderRepository(eventPublisher)

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

    @Bean
    fun getOrdersUseCase(kitchenOrderExtractor: KitchenOrderExtractor) = GetOrdersUseCase(kitchenOrderExtractor)

    @Bean
    fun getOrderById(kitchenOrderExtractor: KitchenOrderExtractor) = GetOrderByIdUseCase(kitchenOrderExtractor)

    @Bean
    fun kitchenOrderController(getOrders: GetOrders, cookOrder: CookOrder) =
        KitchenOrderController(getOrders, cookOrder)

    @Bean
    fun cookOrderEndpoint(cookOrder: CookOrder) = CookOrderEndpoint(cookOrder)

    @Bean
    fun getOrderByIdEndpoint(getOrderById: GetOrderById) = GetOrderByIdEndpoint(getOrderById)
}