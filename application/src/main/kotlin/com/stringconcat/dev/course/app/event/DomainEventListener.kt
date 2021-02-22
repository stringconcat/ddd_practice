package com.stringconcat.dev.course.app.event

import com.stringconcat.ddd.common.types.base.DomainEvent
import kotlin.reflect.KClass

interface DomainEventListener<T : DomainEvent> {

    fun eventType(): KClass<in T>

    fun handle(event: T)
}
