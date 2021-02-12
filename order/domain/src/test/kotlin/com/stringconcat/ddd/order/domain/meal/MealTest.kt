package com.stringconcat.ddd.order.domain.meal

import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class MealTest {

    @Test
    fun `create meal - success`() {

        val mealId = mealId()
        val price = price()
        val name = mealName()
        val description = mealDescription()
        val address = address()

        val result = Meal.addMeal(
            id = { mealId },
            mealExists = { false },
            name = name,
            description = description,
            address = address,
            price = price
        )

        result shouldBeRight {
            it.address shouldBe address
            it.id shouldBe mealId
            it.name shouldBe name
            it.description shouldBe description
            it.price shouldBe price
            it.visible() shouldBe true
            it.popEvents() shouldContainExactly listOf(MealAdded(mealId))
        }
    }

    @Test
    fun `create meal - already exists with the same name`() {

        val mealId = mealId()
        val price = price()
        val name = mealName()
        val description = mealDescription()
        val address = address()

        val result = Meal.addMeal(
            id = { mealId },
            mealExists = { true },
            name = name,
            description = description,
            address = address,
            price = price
        )

        result shouldBeLeft AddMealError.AlreadyExistsWithSameName
    }
}