package com.stringconcat.ddd.delivery.domain

import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class MealTest {

    @Test
    fun `create meal - success`() {
        val name = "Some string"
        val result = Meal.from(name)

        result shouldBeRight {
            it.value shouldBe name
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["", " "])
    fun `create meal - empty string`(input: String) {
        val result = Meal.from(input)
        result shouldBeLeft EmptyMealNameError
    }
}
