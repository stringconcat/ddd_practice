package com.stringconcat.ddd.shop.domain.menu

import arrow.core.Either
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class MealDescriptionTest {

    @Test
    fun `create description - success`() {
        val value = "Some string"
        val result = MealDescription.from(value)

        result.shouldBeInstanceOf<Either.Right<MealDescription>>()
        val description = result.shouldBeRight()
        description.toStringValue() shouldBe value
    }

    @ParameterizedTest
    @ValueSource(strings = ["", " "])
    fun `create description - empty string`(input: String) {
        val result = MealDescription.from(input)
        result shouldBeLeft CreateMealDescriptionError.EmptyDescriptionError
    }
}