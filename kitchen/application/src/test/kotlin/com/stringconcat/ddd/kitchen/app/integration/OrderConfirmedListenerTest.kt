package com.stringconcat.ddd.kitchen.app.integration

import arrow.core.right
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.stringconcat.ddd.kitchen.app.MockCreateOrder
import com.stringconcat.ddd.kitchen.app.RabbitTestConfiguration
import com.stringconcat.ddd.kitchen.usecase.order.CreateOrderRequest
import io.kotest.common.ExperimentalKotest
import io.kotest.common.runBlocking
import io.kotest.framework.concurrency.eventually
import org.junit.jupiter.api.Test
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@ExperimentalKotest
@SpringBootTest(classes = [RabbitTestConfiguration::class])
internal class OrderConfirmedListenerTest {

    @Autowired
    private lateinit var mockCreateOrder: MockCreateOrder

    @Autowired
    private lateinit var rabbitTemplate: RabbitTemplate

    @Autowired
    private lateinit var queue: Queue

    private val mapper = jacksonObjectMapper()
    private val request = OrderConfirmedMessage(id = 1L, items = listOf(OrderMessageItem(mealName = "meal", count = 2)))

    @Test
    fun `order created successfully`() {
        mockCreateOrder.response = Unit.right()
        rabbitTemplate.convertAndSend(queue.name, mapper.writeValueAsString(request))

        runBlocking {
            eventually({
                duration = 5000
                initialDelay = 1000
            }) {
                val request = CreateOrderRequest(id = request.id, items = listOf(
                    CreateOrderRequest.OrderItemData(
                        mealName = request.items[0].mealName,
                        count = request.items[0].count)))
                mockCreateOrder.verifyInvoked(request)
            }
        }
    }
}