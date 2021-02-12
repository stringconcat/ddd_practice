package com.stringconcat.ddd.common.types.base

import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test

internal class EventIdTest {

    @Test
    fun `create event - check event id is unique`() {
        val firstEvent = EmptyEvent()
        val secondEvent = EmptyEvent()
        firstEvent.id shouldNotBe secondEvent.id
        firstEvent.id.value shouldNotBe secondEvent.id.value
    }

    class EmptyEvent: DomainEvent()
}