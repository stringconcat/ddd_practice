package com.stringconcat.ddd.common.types.base

import kotlin.random.Random

open class DomainEntity<T> protected constructor(
    val id: T,
    val version: Version
) {

    private var events = ArrayList<DomainEvent>()

    protected fun addEvent(event: DomainEvent) {
        events.add(event)
    }

    fun popEvents(): List<DomainEvent> {
        val res = events
        events = ArrayList()
        return res
    }
}

class Version internal constructor(val value: Long) : ValueObject {
    companion object {
        fun generate() = Version(Random.nextLong())
    }
}