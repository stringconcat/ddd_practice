package com.stringconcat.ddd.kitchen.domain.order

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class MealTest {

    @Test
    fun `create meal - success`() {
        val name = "Some string"
        val result = Meal.from(name)

        val meal = result.shouldBeRight()
        meal.value shouldBe name
    }

    @ParameterizedTest
    @ValueSource(strings = ["", " "])
    fun `create meal - empty string`(input: String) {
        val result = Meal.from(input)
        result shouldBeLeft EmptyMealNameError
    }
}