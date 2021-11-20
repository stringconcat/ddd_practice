package com.stringconcat.ddd.common.types.base

import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test

internal class DomainEntityTest {

    @Test
    fun `produce event - event stack is clean`() {

        val id = 1L
        val version = Version.new()

        val entity = TestEntity(id, version)
        entity.doSomething()

        entity.id shouldBe id
        entity.version shouldBe version.next()

        val firstInvocationEvents = entity.popEvents()
        firstInvocationEvents.size shouldBeExactly 1
        val firstInvocationEvent = firstInvocationEvents.first()

        entity.doSomething()
        val secondInvocationEvents = entity.popEvents()
        secondInvocationEvents.size shouldBeExactly 1
        val secondInvocationEvent = secondInvocationEvents.first()

        firstInvocationEvent shouldNotBe secondInvocationEvent
    }

    @Test
    fun `version is incremented olny single times after altering entity`() {
        val id = 1L
        val version = Version.new()
        val entity = TestEntity(id, version)
        repeat(10) {
            entity.doSomething()
        }

        entity.version shouldBe version.next()
    }

    @Test
    fun `version is incremented after poping events`() {
        val id = 1L
        val version = Version.new()
        val entity = TestEntity(id, version)
        entity.doSomething()
        entity.popEvents()

        entity.doSomething()

        entity.version shouldBe version.next().next()
    }
}

internal class TestEntity(id: Long, version: Version) : DomainEntity<Long>(id, version, emptyList()) {

    fun doSomething() {
        addEvent(TestEvent())
    }
}

internal class TestEvent : DomainEvent()