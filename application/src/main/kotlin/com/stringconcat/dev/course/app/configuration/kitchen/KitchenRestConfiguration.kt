package com.stringconcat.dev.course.app.configuration.kitchen

import com.stringconcat.ddd.kitchen.rest.order.CookOrderEndpoint
import com.stringconcat.ddd.kitchen.rest.order.GetOrderByIdEndpoint
import com.stringconcat.ddd.kitchen.usecase.order.CookOrder
import com.stringconcat.ddd.kitchen.usecase.order.GetOrderById
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class KitchenRestConfiguration {
    @Bean
    fun cookOrderEndpoint(cookOrder: CookOrder) = CookOrderEndpoint(cookOrder)

    @Bean
    fun getOrderByIdEndpoint(getOrderById: GetOrderById) = GetOrderByIdEndpoint(getOrderById)
}