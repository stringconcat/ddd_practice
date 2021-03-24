package com.stringconcat.dev.course.app.configuration

import com.stringconcat.ddd.kitchen.usecase.order.CreateOrderHandler
import com.stringconcat.ddd.shop.usecase.menu.MealExtractor
import com.stringconcat.ddd.shop.usecase.order.ShopOrderExtractor
import com.stringconcat.dev.course.app.event.EventPublisherImpl
import com.stringconcat.dev.course.app.listeners.SendOrderToKitchenAfterConfirmationRule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ContextsIntegration {

    @Bean
    fun shopOrderConfirmedListener(
        shopOrderExtractor: ShopOrderExtractor,
        mealExtractor: MealExtractor,
        createOrderHandler: CreateOrderHandler,
        domainEventPublisher: EventPublisherImpl
    ): SendOrderToKitchenAfterConfirmationRule {

        val listener = SendOrderToKitchenAfterConfirmationRule(
            shopOrderExtractor = shopOrderExtractor,
            mealExtractor = mealExtractor,
            createOrder = createOrderHandler
        )

        domainEventPublisher.registerListener(listener)
        return listener
    }
}