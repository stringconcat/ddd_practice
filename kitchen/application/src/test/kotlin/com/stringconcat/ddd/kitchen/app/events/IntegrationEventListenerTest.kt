package com.stringconcat.ddd.kitchen.app.events

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class IntegrationEventListenerTest {

    @Test
    fun `data class restored`() {
        val listener = TestEventListener()
        listener.parseMessage("""{"foo":"foo"}""")
        listener.dto.foo shouldBe "foo"
    }

    @Test
    fun `data class restored with unknown fields`() {
        val listener = TestEventListener()
        listener.parseMessage("""{"foo":"foo", "bar":"bar"}""")
        listener.dto.foo shouldBe "foo"
    }

    data class SimpleDto(val foo: String = "bar")

    class TestEventListener : IntegrationEventListener<SimpleDto>(SimpleDto::class.java) {
        lateinit var dto: SimpleDto
        override fun onMessage(message: SimpleDto) {
            dto = message
        }
    }
}