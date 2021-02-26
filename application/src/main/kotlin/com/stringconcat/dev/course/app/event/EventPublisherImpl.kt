package com.stringconcat.dev.course.app.event

import com.stringconcat.ddd.common.types.base.DomainEvent
import com.stringconcat.ddd.common.types.base.EventPublisher
import org.slf4j.LoggerFactory

import kotlin.reflect.KClass

class EventPublisherImpl : EventPublisher {

    private val logger = LoggerFactory.getLogger(EventPublisherImpl::class.java)
    private val listenerMap = HashMap<KClass<*>, MutableList<DomainEventListener<out DomainEvent>>>()

    fun registerListener(listener: DomainEventListener<out DomainEvent>) {
        listenerMap.compute(listener.eventType()) { _, value ->
            val list = value ?: ArrayList()
            list.add(listener)
            list
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun publish(events: Collection<DomainEvent>) {
        events.forEach { e ->
            logger.info("Processing event: $e")
            listenerMap[e::class]?.let { listeners ->
                sendEvents(listeners as List<DomainEventListener<in DomainEvent>>, e)
            }
        }
    }

    private fun sendEvents(listeners: List<DomainEventListener<in DomainEvent>>, event: DomainEvent) {
        listeners.forEach { l -> l.handle(event) }
    }
}