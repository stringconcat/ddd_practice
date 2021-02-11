package com.stringconcat.ddd.common.types.base

import io.kotlintest.matchers.numerics.shouldBeExactly
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import org.junit.jupiter.api.Test

internal class DomainEntityTest {

    @Test
    fun `create entity - success (jacoco workaround)`(){

        val version = Version.generate()
        val entity = TestEntity(1L, version)

        entity.id shouldBe 1L
        entity.version shouldBe version
    }


    @Test
    fun `produce event - event stack is clean`() {

        val entity = TestEntity(1L, Version.generate())
        entity.generateEvent()

        val firstInvocationEvents = entity.popEvents()
        firstInvocationEvents.size shouldBeExactly 1
        val firstInvocationEvent = firstInvocationEvents.first()


        entity.generateEvent()
        val secondInvocationEvents = entity.popEvents()
        secondInvocationEvents.size shouldBeExactly 1
        val secondInvocationEvent = secondInvocationEvents.first()

        firstInvocationEvent shouldNotBe secondInvocationEvent

    }

}

internal class TestEntity(id: Long, version: Version) : DomainEntity<Long>(id, version) {

    fun generateEvent(){
        addEvent(TestEvent())
    }
}

internal class TestEvent: DomainEvent()