package com.stringconcat.ddd.shop.telnet.menu

import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class LongParameterExtractorTest {

    @Test
    fun `parse success`() {
        val result = LongParameterExtractor.extract("add 123")
        result shouldBeRight 123
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "add 123 123", "add   123", "lline", "    ", "add 123 "])
    fun `parse fail`(line: String) {
        val result = LongParameterExtractor.extract(line)
        result.shouldBeLeft()
    }
}