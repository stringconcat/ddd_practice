package com.stringconcat.ddd.shop.domain.menu

import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class MealNameTest {

    @Test
    fun `create name - success`() {
        val name = "Some string"
        val result = MealName.from(name)

        result shouldBeRight {
            it.value shouldBe name
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["", " "])
    fun `create name - empty string`(input: String) {
        val result = MealName.from(input)
        result shouldBeLeft EmptyMealNameError
    }
}