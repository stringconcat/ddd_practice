package com.stringconcat.ddd.shop.app.event

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.core.RabbitTemplate

class RabbitMessagePublisher(
    private val template: RabbitTemplate,
    private val queue: Queue,
) : IntegrationMessagePublisher {

    private val objectMapper = jacksonObjectMapper()

    override fun send(message: Any) {
        template.convertAndSend(queue.name, objectMapper.writeValueAsString(message))
    }
}