package com.stringconcat.ddd.common.domain.supertype

import io.kotlintest.shouldNotBe
import org.junit.jupiter.api.Test

internal class EventIdTest {

    @Test
    fun `create event - check event id is unique`() {
        val firstEvent = EmptyEvent()
        val secondEvent = EmptyEvent()
        firstEvent.id shouldNotBe secondEvent.id
    }


    class EmptyEvent: DomainEvent()
}