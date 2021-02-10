package com.stringconcat.ddd.common.domain.supertype

import java.util.*

abstract class DomainEvent {
    val id = EventId.generate()
}

open class EventId internal constructor(val id: UUID) {

    companion object {
        fun generate(): EventId = EventId(UUID.randomUUID())
    }
}

