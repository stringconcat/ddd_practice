package com.stringconcat.ddd.shop.app.configuration

import com.stringconcat.ddd.shop.app.event.EventPublisherImpl
import com.stringconcat.ddd.shop.app.event.IntegrationMessagePublisher
import com.stringconcat.ddd.shop.app.listeners.SendOrderToKitchenAfterConfirmationRule
import com.stringconcat.ddd.shop.usecase.menu.access.MealExtractor
import com.stringconcat.ddd.shop.usecase.order.access.ShopOrderExtractor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ContextsIntegration {

    @Bean
    fun shopOrderConfirmedListener(
        shopOrderExtractor: ShopOrderExtractor,
        mealExtractor: MealExtractor,
        domainEventPublisher: EventPublisherImpl,
        integrationMessagePublisher: IntegrationMessagePublisher
    ): SendOrderToKitchenAfterConfirmationRule {

        val listener = SendOrderToKitchenAfterConfirmationRule(
            shopOrderExtractor = shopOrderExtractor,
            mealExtractor = mealExtractor,
            integrationMessagePublisher
        )

        domainEventPublisher.registerListener(listener)
        return listener
    }
}