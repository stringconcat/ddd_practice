package com.stringconcat.dev.course.app.listeners

import com.stringconcat.ddd.kitchen.usecase.order.CreateOrder
import com.stringconcat.ddd.kitchen.usecase.order.CreateOrderRequest
import com.stringconcat.ddd.shop.domain.order.ShopOrderConfirmedDomainEvent
import com.stringconcat.ddd.shop.usecase.menu.MealExtractor
import com.stringconcat.ddd.shop.usecase.order.ShopOrderExtractor
import com.stringconcat.dev.course.app.event.DomainEventListener

class SendOrderToKitchenAfterConfirmationRule(
    private val shopOrderExtractor: ShopOrderExtractor,
    private val mealExtractor: MealExtractor,
    private val createOrder: CreateOrder
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

            CreateOrderRequest.OrderItemData(
                mealName = meal.name.value,
                count = it.count.value
            )
        }
        val request = CreateOrderRequest(id = order.id.value, items = itemData)
        createOrder.execute(request).mapLeft {
            error("Cannot create order #${order.id} for kitchen: $it")
        }
    }
}