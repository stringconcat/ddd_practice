package com.stringconcat.ddd.order.domain.menu

import com.stringconcat.ddd.order.domain.mealDescription
import com.stringconcat.ddd.order.domain.mealId
import com.stringconcat.ddd.order.domain.mealName
import com.stringconcat.ddd.order.domain.price
import com.stringconcat.ddd.order.domain.address
import com.stringconcat.ddd.order.domain.meal
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class MealTest {

    @Test
    fun `add meal - success`() {

        val mealId = mealId()
        val price = price()
        val name = mealName()
        val description = mealDescription()
        val address = address()

        val result = Meal.addMealToMenu(
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
            it.popEvents() shouldContainExactly listOf(MealHasBeenAddedToMenu(mealId))
        }
    }

    @Test
    fun `add meal to menu - already exists with the same name`() {

        val mealId = mealId()
        val price = price()
        val name = mealName()
        val description = mealDescription()
        val address = address()

        val result = Meal.addMealToMenu(
            id = { mealId },
            mealExists = { true },
            name = name,
            description = description,
            address = address,
            price = price
        )

        result shouldBeLeft AddMealToMenuError.AlreadyExistsWithSameName
    }

    @Test
    fun `remove meal from menu - success`() {
        val meal = meal(removed = false)
        meal.removeMealFromMenu()

        meal.removed shouldBe true
        meal.visible() shouldBe false
        meal.popEvents() shouldContainExactly listOf(MealHasBeenRemovedFromMenu(meal.id))
    }

    @Test
    fun `remove meal from menu - already removed`() {
        val meal = meal(removed = true)
        meal.removeMealFromMenu()

        meal.removed shouldBe true
        meal.visible() shouldBe false
        meal.popEvents() shouldContainExactly emptyList()
    }
}