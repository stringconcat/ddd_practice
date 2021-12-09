package com.stringconcat.ddd.shop.app.event

import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.core.RabbitTemplate

class RabbitMessagePublisher(
    private val template: RabbitTemplate,
    private val queue: Queue,
) : IntegrationMessagePublisher {

    override fun send(message: Any) {
        template.convertAndSend(queue.name, message)
    }
}