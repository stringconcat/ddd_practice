package com.stringconcat.dev.course.app.event

import com.stringconcat.ddd.common.types.base.DomainEvent
import io.kotest.matchers.collections.shouldContainExactly
import org.junit.jupiter.api.Test

class EventPublisherImplTest {

    @Test
    fun `publish events`() {
        val publisher = EventPublisherImpl(listOf(TestEventListener, AnotherTestEventListener))
        val testEvent = TestEvent()
        val anotherTestEvent = AnotherTestEvent()
        val events = listOf(testEvent, anotherTestEvent)

        publisher.publish(events)

        TestEventListener.events shouldContainExactly listOf(testEvent)
        AnotherTestEventListener.events shouldContainExactly listOf(anotherTestEvent)
    }

    object TestEventListener : DomainEventListener<TestEvent> {
        val events = ArrayList<TestEvent>()
        override fun eventType() = TestEvent::class

        override fun handle(event: TestEvent) {
            events.add(event)
        }
    }

    object AnotherTestEventListener : DomainEventListener<AnotherTestEvent> {
        val events = ArrayList<AnotherTestEvent>()
        override fun eventType() = AnotherTestEvent::class

        override fun handle(event: AnotherTestEvent) {
            events.add(event)
        }
    }

    class TestEvent : DomainEvent()
    class AnotherTestEvent : DomainEvent()
}