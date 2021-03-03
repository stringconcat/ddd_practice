package com.stringconcat.ddd.common.types.base

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

    fun increment() = Version(value + 1)

    companion object {
        fun new() = Version(0)
        fun from(value: Long) = Version(value)
    }
}