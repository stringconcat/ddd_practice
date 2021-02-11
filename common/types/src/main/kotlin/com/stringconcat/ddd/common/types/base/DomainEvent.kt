package com.stringconcat.ddd.common.types.base

import java.util.UUID

open class DomainEvent protected constructor() {
    val id = EventId.generate()
}

data class EventId internal constructor(val value: UUID) {
    companion object {
        fun generate(): EventId = EventId(UUID.randomUUID())
    }
}