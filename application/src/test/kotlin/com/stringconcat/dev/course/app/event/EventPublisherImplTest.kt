package com.stringconcat.dev.course.app.event

import com.stringconcat.ddd.common.types.base.DomainEvent
import io.kotest.matchers.collections.shouldContainExactly
import org.junit.jupiter.api.Test

class EventPublisherImplTest {

    @Test
    fun `publish events`() {
        val publisher = EventPublisherImpl()

        val testEventListener = TestEventListener()
        publisher.registerListener(testEventListener)

        val anotherTestEventListener = AnotherTestEventListener()
        publisher.registerListener(anotherTestEventListener)

        val testEvent = TestEvent("TestEvent")
        val anotherTestEvent = AnotherTestEvent("AnotherTestEvent")
        val events = listOf(testEvent, anotherTestEvent)

        publisher.publish(events)

        testEventListener.events shouldContainExactly listOf(testEvent)
        anotherTestEventListener.events shouldContainExactly listOf(anotherTestEvent)
    }

    class TestEventListener : DomainEventListener<TestEvent> {
        val events = ArrayList<TestEvent>()
        override fun eventType() = TestEvent::class

        override fun handle(event: TestEvent) {
            events.add(event)
        }
    }

    class AnotherTestEventListener : DomainEventListener<AnotherTestEvent> {
        val events = ArrayList<AnotherTestEvent>()
        override fun eventType() = AnotherTestEvent::class

        override fun handle(event: AnotherTestEvent) {
            events.add(event)
        }
    }

    data class TestEvent(val name: String) : DomainEvent()
    data class AnotherTestEvent(val name: String) : DomainEvent()
}