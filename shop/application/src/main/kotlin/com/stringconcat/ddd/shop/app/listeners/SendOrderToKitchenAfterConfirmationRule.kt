package com.stringconcat.ddd.shop.app.listeners

import com.stringconcat.ddd.common.events.DomainEventListener
import com.stringconcat.ddd.shop.domain.order.ShopOrderConfirmedDomainEvent
import com.stringconcat.ddd.shop.usecase.menu.access.MealExtractor
import com.stringconcat.ddd.shop.usecase.order.access.ShopOrderExtractor

@Suppress("UnusedPrivateMember")
class SendOrderToKitchenAfterConfirmationRule(
    private val shopOrderExtractor: ShopOrderExtractor,
    private val mealExtractor: MealExtractor
) : DomainEventListener<ShopOrderConfirmedDomainEvent> {

    override fun eventType() = ShopOrderConfirmedDomainEvent::class

    override fun handle(event: ShopOrderConfirmedDomainEvent) {
        val order = shopOrderExtractor.getById(event.orderId)
        checkNotNull(order) {
            "Shop order #${event.orderId} not found"
        }

//        val itemData = order.orderItems.map {
//            val meal = mealExtractor.getById(it.mealId)
//            checkNotNull(meal) {
//                "Meal #${it.mealId} not found"
//            }
//
//            CreateOrderRequest.OrderItemData(
//                mealName = meal.name.toStringValue(),
//                count = it.count.toIntValue()
//            )
//        }
//        val request = CreateOrderRequest(id = order.id.toLongValue(), items = itemData)
//        createOrder.execute(request).mapLeft {
//            error("Cannot create order #${order.id} for kitchen: $it")
//        }
    }
}