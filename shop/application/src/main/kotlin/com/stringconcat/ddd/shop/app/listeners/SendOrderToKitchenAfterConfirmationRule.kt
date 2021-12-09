package com.stringconcat.ddd.shop.app.listeners

import com.stringconcat.ddd.common.events.DomainEventListener
import com.stringconcat.ddd.shop.app.event.IntegrationMessagePublisher
import com.stringconcat.ddd.shop.domain.order.ShopOrderConfirmedDomainEvent
import com.stringconcat.ddd.shop.usecase.menu.access.MealExtractor
import com.stringconcat.ddd.shop.usecase.order.access.ShopOrderExtractor

class SendOrderToKitchenAfterConfirmationRule(
    private val shopOrderExtractor: ShopOrderExtractor,
    private val mealExtractor: MealExtractor,
    private val integrationMessagePublisher: IntegrationMessagePublisher,
) : DomainEventListener<ShopOrderConfirmedDomainEvent> {

    override fun eventType() = ShopOrderConfirmedDomainEvent::class

    override fun handle(event: ShopOrderConfirmedDomainEvent) {
        val order = shopOrderExtractor.getById(event.orderId)
        checkNotNull(order) {
            "Shop order #${event.orderId} not found"
        }

        val itemData = order.orderItems.map {
            val meal = mealExtractor.getById(it.mealId)
            checkNotNull(meal) {
                "Meal #${it.mealId} not found"
            }
            OrderMessageItem(
                mealName = meal.name.toStringValue(),
                count = it.count.toIntValue()
            )
        }
        val message = OrderConfirmedMessage(id = order.id.toLongValue(), items = itemData)
        integrationMessagePublisher.send(message)
    }
}

data class OrderConfirmedMessage(val id: Long, val items: List<OrderMessageItem>)

data class OrderMessageItem(val mealName: String, val count: Int)