package com.stringconcat.ddd.kitchen.app.configuration.kitchen

import com.stringconcat.ddd.kitchen.rest.order.CookOrderEndpoint
import com.stringconcat.ddd.kitchen.rest.order.GetOrderByIdEndpoint
import com.stringconcat.ddd.kitchen.rest.order.GetOrdersEndpoint
import com.stringconcat.ddd.kitchen.usecase.order.CookOrder
import com.stringconcat.ddd.kitchen.usecase.order.GetOrderById
import com.stringconcat.ddd.kitchen.usecase.order.GetOrders
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class KitchenRestConfiguration {
    @Bean
    fun kitchenGetOrdersEndpoint(getOrders: GetOrders) = GetOrdersEndpoint(getOrders)

    @Bean
    fun cookOrderEndpoint(cookOrder: CookOrder) = CookOrderEndpoint(cookOrder)

    @Bean
    fun kitchenGetOrderByIdEndpoint(getOrderById: GetOrderById) = GetOrderByIdEndpoint(getOrderById)
}