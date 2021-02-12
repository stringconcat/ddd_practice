package com.stringconcat.ddd.order.domain.meal

import arrow.core.Either
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class DescriptionTest {

    @Test
    fun `create description - success`() {
        val description = "Some string"
        val result = Description.fromString(description)

        result.shouldBeInstanceOf<Either.Right<Description>>()
        result shouldBeRight {
            it.value shouldBe description
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["", " "])
    fun `create description - empty string`(input: String) {
        val result = Description.fromString(input)
        result shouldBeLeft CreateDescriptionError.EmptyString
    }
}