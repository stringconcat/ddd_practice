package com.stringconcat.ddd.kitchen.app.configuration

import com.stringconcat.ddd.kitchen.app.integration.OrderConfirmedListener
import com.stringconcat.ddd.kitchen.usecase.order.CreateOrder
import org.springframework.amqp.core.Queue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MessagingConfiguration {

    @Bean
    fun queue(): Queue {
        return Queue("confirm")
    }

    @Bean
    fun orderConfirmedListener(createOrder: CreateOrder) = OrderConfirmedListener(createOrder)
}