package com.stringconcat.ddd.shop.app.configuration

import com.stringconcat.ddd.shop.app.event.RabbitMessagePublisher
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MessagingConfiguration {

    @Bean
    fun queue(): Queue {
        return Queue("confirm")
    }

    @Bean
    fun rabbitMessagePublisher(template: RabbitTemplate, queue: Queue) = RabbitMessagePublisher(template, queue)
}