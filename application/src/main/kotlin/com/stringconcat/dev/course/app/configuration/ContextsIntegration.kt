package com.stringconcat.dev.course.app.configuration

import com.stringconcat.ddd.kitchen.usecase.order.CreateOrderHandler
import com.stringconcat.ddd.shop.usecase.menu.MealExtractor
import com.stringconcat.ddd.shop.usecase.order.CustomerOrderExtractor
import com.stringconcat.dev.course.app.event.EventPublisherImpl
import com.stringconcat.dev.course.app.listeners.SendOrderToKitchenAfterConfirmationRule
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
    ): SendOrderToKitchenAfterConfirmationRule {

        val listener = SendOrderToKitchenAfterConfirmationRule(
            customerOrderExtractor = customerOrderExtractor,
            mealExtractor = mealExtractor,
            createOrder = createOrderHandler
        )

        domainEventPublisher.registerListener(listener)
        return listener
    }
}