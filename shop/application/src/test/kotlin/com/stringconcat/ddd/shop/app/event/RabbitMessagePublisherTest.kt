package com.stringconcat.ddd.shop.app.event

import io.kotest.common.runBlocking
import io.kotest.framework.concurrency.eventually
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import org.testcontainers.containers.RabbitMQContainer

@SpringBootTest(classes = [RabbitMessagePublisherTest.TestConfiguration::class])
@io.kotest.common.ExperimentalKotest
class RabbitMessagePublisherTest {

    @Autowired
    private lateinit var publisher: RabbitMessagePublisher

    @Autowired
    private lateinit var receiver: MockReceiver

    @Test
    fun `message sent successfully`() {
        publisher.send(SimpleDto())
        receiver.verifyInvoked("""{"foo":"bar"}""")
    }

    @Configuration
    class TestConfiguration {

        @Bean
        fun queue() = Queue("testQueue")

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
        fun publisher(template: RabbitTemplate, queue: Queue) = RabbitMessagePublisher(template, queue)

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
        fun listenerAdapter(receiver: MockReceiver) =
            MessageListenerAdapter(receiver, "receive")

        @Bean
        fun admin(connectionFactory: ConnectionFactory, queue: Queue) =
            RabbitAdmin(connectionFactory).apply {
                declareQueue(queue)
            }

        @Bean
        fun receiver() = MockReceiver()
    }

    data class SimpleDto(val foo: String = "bar")

    class MockReceiver {
        lateinit var message: String

        fun receive(message: String) {
            this.message = message
        }

        fun verifyInvoked(message: String) {
            runBlocking {
                eventually({
                    duration = 5000
                    initialDelay = 1000
                }) {
                    this.message shouldBe message
                }
            }
        }
    }
}