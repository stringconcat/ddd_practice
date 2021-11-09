package com.stringconcat.ddd.common.events

import com.stringconcat.ddd.common.types.base.DomainEvent
import kotlin.reflect.KClass

interface DomainEventListener<T : DomainEvent> {

    fun eventType(): KClass<T>

    fun handle(event: T)
}
