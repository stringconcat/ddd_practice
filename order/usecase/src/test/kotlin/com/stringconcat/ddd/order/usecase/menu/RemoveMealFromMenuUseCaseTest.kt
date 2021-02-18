package com.stringconcat.ddd.order.usecase.menu

import com.stringconcat.ddd.order.domain.menu.MealHasBeenRemovedFromMenu
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.maps.shouldContain
import io.kotest.matchers.maps.shouldContainExactly
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class RemoveMealFromMenuUseCaseTest {

    private val mealPersister = TestMealPersister()
    private val mealExtractor = TestMealExtractor()

    @Test
    fun `successfully removed`() {
        val meal = meal()
        mealExtractor[meal.id] = meal
        val useCase = RemoveMealFromMenuUseCase(mealExtractor, mealPersister)
        val result = useCase.removeMealFromMenu(meal.id.value)
        result shouldBeRight Unit

        mealPersister shouldContain (meal.id to meal)
        val storedMeal = mealPersister[meal.id]

        storedMeal.shouldNotBeNull()
        storedMeal shouldBe meal
        storedMeal.popEvents() shouldContainExactly listOf(MealHasBeenRemovedFromMenu(meal.id))
    }

    @Test
    fun `meal not found`() {
        val useCase = RemoveMealFromMenuUseCase(mealExtractor, mealPersister)
        val result = useCase.removeMealFromMenu(mealId().value)
        result shouldBeLeft MealNotFound
        mealPersister shouldContainExactly emptyMap()
    }
}