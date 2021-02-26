package com.stringconcat.dev.course.app.configuration

import com.stringconcat.ddd.kitchen.usecase.order.CreateOrderHandler
import com.stringconcat.ddd.order.usecase.menu.MealExtractor
import com.stringconcat.ddd.order.usecase.order.CustomerOrderExtractor
import com.stringconcat.dev.course.app.event.EventPublisherImpl
import com.stringconcat.dev.course.app.kitchen.CustomerOrderConfirmedListener
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ContextsIntegration {

    @Bean
    fun customerOrderConfirmedListener(
        customerOrderExtractor: CustomerOrderExtractor,
        mealExtractor: MealExtractor,
        createOrderHandler: CreateOrderHandler,
        domainEventPublisher: EventPublisherImpl
    ): CustomerOrderConfirmedListener {

        val listener = CustomerOrderConfirmedListener(
            customerOrderExtractor = customerOrderExtractor,
            mealExtractor = mealExtractor,
            createOrderHandler = createOrderHandler
        )

        domainEventPublisher.registerListener(listener)
        return listener
    }
}