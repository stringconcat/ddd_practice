package com.stringconcat.ddd.kitchen.app

import com.stringconcat.ddd.kitchen.app.integration.OrderConfirmedListener
import com.stringconcat.ddd.kitchen.usecase.order.CreateOrder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import org.testcontainers.containers.RabbitMQContainer

@Configuration
class RabbitTestConfiguration {

    @Bean
    fun queue() = Queue("confirm")

    @Bean(initMethod = "start")
    fun rabbitContainer() = RabbitMQContainer("rabbitmq:3.9.11")

    @Bean
    fun connectionFactory(rabbitMQContainer: RabbitMQContainer) =
        CachingConnectionFactory().apply {
            port = rabbitMQContainer.amqpPort
            host = rabbitMQContainer.host
            setPassword(rabbitMQContainer.adminPassword)
            username = rabbitMQContainer.adminUsername
        }

    @Bean
    fun template(factory: CachingConnectionFactory) = RabbitTemplate(factory)

    @Bean
    @DependsOn("admin")
    fun container(
        connectionFactory: ConnectionFactory,
        listenerAdapter: MessageListenerAdapter,
        queue: Queue,
    ): SimpleMessageListenerContainer {
        val container = SimpleMessageListenerContainer()
        container.connectionFactory = connectionFactory
        container.setQueueNames(queue.name)
        container.setMessageListener(listenerAdapter)
        return container
    }

    @Bean
    fun listenerAdapter(receiver: OrderConfirmedListener) =
        MessageListenerAdapter(receiver, "parseMessage")

    @Bean
    fun admin(connectionFactory: ConnectionFactory, queue: Queue) =
        RabbitAdmin(connectionFactory).apply {
            declareQueue(queue)
        }

    @Bean
    fun createOrder() = MockCreateOrder()

    @Bean
    fun listener(createOrder: CreateOrder) = OrderConfirmedListener(createOrder)
}