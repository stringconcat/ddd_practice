package com.stringconcat.dev.course.app.configuration

import com.stringconcat.ddd.common.types.base.EventPublisher
import com.stringconcat.ddd.kitchen.persistence.order.InMemoryKitchenOrderRepository
import com.stringconcat.ddd.kitchen.usecase.order.CookOrder
import com.stringconcat.ddd.kitchen.usecase.order.CookOrderUseCase
import com.stringconcat.ddd.kitchen.usecase.order.CreateOrderHandler
import com.stringconcat.ddd.kitchen.usecase.order.GetOrders
import com.stringconcat.ddd.kitchen.usecase.order.GetOrdersUseCase
import com.stringconcat.ddd.kitchen.usecase.order.KitchenOrderExtractor
import com.stringconcat.ddd.kitchen.usecase.order.KitchenOrderPersister
import com.stringconcat.ddd.kitchen.web.order.KitchenOrderController
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

    @Bean
    fun getOrdersUseCase(kitchenOrderExtractor: KitchenOrderExtractor) = GetOrdersUseCase(kitchenOrderExtractor)

    @Bean
    fun kitchenOrderController(getOrders: GetOrders, cookOrder: CookOrder) =
        KitchenOrderController(getOrders, cookOrder)
}