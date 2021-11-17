package com.stringconcat.ddd.common.types.base

open class DomainEntity<T> protected constructor(
    val id: T,
    var version: Version,
) {

    private var events = ArrayList<DomainEvent>()

    protected fun addEvent(event: DomainEvent) {
        if (events.isEmpty()) {
            version = version.next()
        }
        events.add(event)
    }

    fun popEvents(): List<DomainEvent> {
        val res = events
        events = ArrayList()
        return res
    }
}

data class Version internal constructor(val value: Long) : ValueObject {

    fun next() = Version(value + 1)

    fun previous() = Version(value - 1)

    fun isNew() = value == START

    companion object {

        private const val START = 0L

        fun new() = Version(START)
        fun from(value: Long) = Version(value)
    }
}