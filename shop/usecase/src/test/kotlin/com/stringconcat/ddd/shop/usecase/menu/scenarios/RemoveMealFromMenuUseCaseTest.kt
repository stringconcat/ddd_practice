package com.stringconcat.ddd.shop.usecase.menu.scenarios

import com.stringconcat.ddd.shop.domain.menu.MealRemovedFromMenuDomainEvent
import com.stringconcat.ddd.shop.usecase.TestMealExtractor
import com.stringconcat.ddd.shop.usecase.TestMealPersister
import com.stringconcat.ddd.shop.usecase.meal
import com.stringconcat.ddd.shop.usecase.mealId
import com.stringconcat.ddd.shop.usecase.menu.RemoveMealFromMenuUseCaseError
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.maps.shouldContain
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class RemoveMealFromMenuUseCaseTest {

    @Test
    fun `successfully removed`() {

        val meal = meal()
        val mealPersister = TestMealPersister()
        val mealExtractor = TestMealExtractor().apply {
            this[meal.id] = meal
        }

        val useCase = RemoveMealFromMenuUseCase(mealExtractor, mealPersister)
        val result = useCase.execute(meal.id)
        result shouldBeRight Unit

        mealPersister shouldContain (meal.id to meal)
        val storedMeal = mealPersister[meal.id]

        storedMeal.shouldNotBeNull()
        storedMeal shouldBe meal
        storedMeal.popEvents() shouldContainExactly listOf(MealRemovedFromMenuDomainEvent(meal.id))
    }

    @Test
    fun `meal not found`() {

        val mealPersister = TestMealPersister()
        val mealExtractor = TestMealExtractor()
        val useCase = RemoveMealFromMenuUseCase(mealExtractor, mealPersister)

        val result = useCase.execute(mealId())
        result shouldBeLeft RemoveMealFromMenuUseCaseError.MealNotFound
        mealPersister.shouldBeEmpty()
    }
}