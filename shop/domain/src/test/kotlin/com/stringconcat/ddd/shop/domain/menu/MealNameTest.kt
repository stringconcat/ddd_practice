package com.stringconcat.ddd.shop.domain.menu

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class MealNameTest {

    @Test
    fun `create name - success`() {
        val value = "Some string"
        val result = MealName.from(value)

        val name = result.shouldBeRight()
        name.value shouldBe value
    }

    @ParameterizedTest
    @ValueSource(strings = ["", " "])
    fun `create name - empty string`(input: String) {
        val result = MealName.from(input)
        result shouldBeLeft CreateMealNameError.EmptyMealNameError
    }
}