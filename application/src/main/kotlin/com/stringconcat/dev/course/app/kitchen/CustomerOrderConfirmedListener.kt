package com.stringconcat.dev.course.app.kitchen

import com.stringconcat.ddd.kitchen.usecase.order.CreateOrderHandler
import com.stringconcat.ddd.kitchen.usecase.order.CreateOrderRequest
import com.stringconcat.ddd.order.domain.order.CustomerOrderConfirmedDomainEvent
import com.stringconcat.ddd.order.usecase.menu.MealExtractor
import com.stringconcat.ddd.order.usecase.order.CustomerOrderExtractor
import com.stringconcat.dev.course.app.event.DomainEventListener
import org.slf4j.LoggerFactory

class CustomerOrderConfirmedListener(
    private val customerOrderExtractor: CustomerOrderExtractor,
    private val mealExtractor: MealExtractor,
    private val createOrderHandler: CreateOrderHandler
) : DomainEventListener<CustomerOrderConfirmedDomainEvent> {

    private val logger = LoggerFactory.getLogger(CustomerOrderConfirmedListener::class.java)

    override fun eventType() = CustomerOrderConfirmedDomainEvent::class

    override fun handle(event: CustomerOrderConfirmedDomainEvent) {
        val order = customerOrderExtractor.getById(event.orderId)
        checkNotNull(order) {
            "Customer order #${event.orderId} not found"
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
        createOrderHandler.execute(request).mapLeft {
            logger.error("Cannot create order #${order.id} for kitchen: $it")
        }
    }
}