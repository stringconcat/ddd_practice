package com.stringconcat.ddd.shop.app.listeners

import com.stringconcat.ddd.common.types.count
import com.stringconcat.ddd.shop.app.MockIntegrationPublisher
import com.stringconcat.ddd.shop.domain.meal
import com.stringconcat.ddd.shop.domain.order
import com.stringconcat.ddd.shop.domain.order.OrderItem
import com.stringconcat.ddd.shop.domain.order.ShopOrderConfirmedDomainEvent
import com.stringconcat.ddd.shop.domain.orderId
import com.stringconcat.ddd.shop.domain.price
import com.stringconcat.ddd.shop.usecase.MockMealExtractor
import com.stringconcat.ddd.shop.usecase.MockShopOrderExtractor
import io.kotest.assertions.throwables.shouldThrow
import org.junit.jupiter.api.Test

class SendOrderToKitchenAfterConfirmationRuleTest {

    @Test
    fun `order successfully sent`() {

        val meal = meal()
        val price = price()
        val count = count()
        val order = order(orderItems = setOf(OrderItem(meal.id, price, count)))

        val orderExtractor = MockShopOrderExtractor(order)
        val mealExtractor = MockMealExtractor(meal)
        val publisher = MockIntegrationPublisher()

        val rule = SendOrderToKitchenAfterConfirmationRule(
            mealExtractor = mealExtractor,
            shopOrderExtractor = orderExtractor,
            integrationMessagePublisher = publisher
        )

        val event = ShopOrderConfirmedDomainEvent(order.id)
        rule.handle(event)

        val expectedMessage = OrderConfirmedMessage(
            id = order.id.toLongValue(),
            items = listOf(OrderMessageItem(
                mealName = meal.name.toStringValue(),
                count = count.toIntValue()))
        )

        publisher.verifyInvoked(expectedMessage)
        orderExtractor.verifyInvokedGetById(order.id)
        mealExtractor.verifyInvokedGetById(meal.id)
    }

    @Test
    fun `order not found`() {
        val orderExtractor = MockShopOrderExtractor()

        val mealExtractor = MockMealExtractor()

        val publisher = MockIntegrationPublisher()

        val rule = SendOrderToKitchenAfterConfirmationRule(
            mealExtractor = mealExtractor,
            shopOrderExtractor = orderExtractor,
            integrationMessagePublisher = publisher
        )

        val orderId = orderId()
        val event = ShopOrderConfirmedDomainEvent(orderId)

        shouldThrow<IllegalStateException> {
            rule.handle(event)
        }

        orderExtractor.verifyInvokedGetById(orderId)
        mealExtractor.verifyEmpty()
        publisher.verifyZeroInteraction()
    }

    @Test
    fun `meal not found`() {

        val meal = meal()
        val price = price()
        val count = count()
        val order = order(orderItems = setOf(OrderItem(meal.id, price, count)))

        val orderExtractor = MockShopOrderExtractor(order)

        val mealExtractor = MockMealExtractor()
        val publisher = MockIntegrationPublisher()

        val rule = SendOrderToKitchenAfterConfirmationRule(
            mealExtractor = mealExtractor,
            shopOrderExtractor = orderExtractor,
            integrationMessagePublisher = publisher
        )

        val event = ShopOrderConfirmedDomainEvent(order.id)

        shouldThrow<IllegalStateException> {
            rule.handle(event)
        }

        orderExtractor.verifyInvokedGetById(order.id)
        mealExtractor.verifyInvokedGetById(meal.id)
        publisher.verifyZeroInteraction()
    }
}