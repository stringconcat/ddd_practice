package com.stringconcat.ddd.kitchen.app.integration

import com.stringconcat.ddd.kitchen.app.events.IntegrationEventListener
import com.stringconcat.ddd.kitchen.usecase.order.CreateOrder
import com.stringconcat.ddd.kitchen.usecase.order.CreateOrderRequest
import org.springframework.amqp.rabbit.annotation.RabbitListener

@RabbitListener(queues = ["confirm"])
class OrderConfirmedListener(private val createOrder: CreateOrder) :
    IntegrationEventListener<OrderConfirmedMessage>(OrderConfirmedMessage::class.java) {

    override fun onMessage(message: OrderConfirmedMessage) {
        val items = message.items
            .map { CreateOrderRequest.OrderItemData(mealName = it.mealName, count = it.count) }
        val request = CreateOrderRequest(id = message.id, items = items)
        createOrder.execute(request).mapLeft {
            error(it)
        }
    }
}

data class OrderConfirmedMessage(val id: Long, val items: List<OrderMessageItem>)

data class OrderMessageItem(val mealName: String, val count: Int)