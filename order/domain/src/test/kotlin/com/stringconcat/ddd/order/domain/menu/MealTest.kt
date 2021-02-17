package com.stringconcat.ddd.order.domain.menu

import com.stringconcat.ddd.order.domain.TestMealAlreadyExistsRule
import com.stringconcat.ddd.order.domain.meal
import com.stringconcat.ddd.order.domain.mealDescription
import com.stringconcat.ddd.order.domain.mealId
import com.stringconcat.ddd.order.domain.mealName
import com.stringconcat.ddd.order.domain.price
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class MealTest {

    val mealId = mealId()

    private val idGenerator = object : MealIdGenerator {
        override fun generateId() = mealId
    }

    @Test
    fun `add meal - success`() {

        val price = price()
        val name = mealName()
        val description = mealDescription()
        val mealExistsRule = TestMealAlreadyExistsRule(false)

        val result = Meal.addMealToMenu(
            idGenerator,
            mealExistsRule = mealExistsRule,
            name = name,
            description = description,
            price = price
        )

        result shouldBeRight {
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

        val mealExistsRule = TestMealAlreadyExistsRule(true)
        val price = price()
        val name = mealName()
        val description = mealDescription()

        val result = Meal.addMealToMenu(
            idGenerator,
            mealExistsRule = mealExistsRule,
            name = name,
            description = description,
            price = price
        )

        result shouldBeLeft AlreadyExistsWithSameNameError
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