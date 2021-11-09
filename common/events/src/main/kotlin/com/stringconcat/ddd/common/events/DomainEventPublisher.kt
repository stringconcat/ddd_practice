package com.stringconcat.ddd.common.events

import com.stringconcat.ddd.common.types.base.DomainEvent

interface DomainEventPublisher {
    fun publish(events: Collection<DomainEvent>)
}