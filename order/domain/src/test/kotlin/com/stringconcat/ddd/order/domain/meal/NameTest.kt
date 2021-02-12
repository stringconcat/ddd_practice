package com.stringconcat.ddd.order.domain.meal

import arrow.core.Either
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class NameTest {

    @Test
    fun `create name - success`() {
        val name = "Some string"
        val result = Name.fromString(name)

        result.shouldBeInstanceOf<Either.Right<Name>>()
        result shouldBeRight {
            it.value shouldBe name
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["", " "])
    fun `create name - empty string`(input: String) {
        val result = Name.fromString(input)
        result shouldBeLeft CreateNameError.EmptyString
    }
}