package com.stringconcat.ddd.shop.usecase.menu.scenarios

import com.stringconcat.ddd.shop.domain.meal
import com.stringconcat.ddd.shop.domain.mealId
import com.stringconcat.ddd.shop.usecase.MockMealExtractor
import com.stringconcat.ddd.shop.usecase.MockMealPersister
import com.stringconcat.ddd.shop.usecase.menu.RemoveMealFromMenuUseCaseError
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import org.junit.jupiter.api.Test

internal class RemoveMealFromMenuUseCaseTest {

    @Test
    fun `successfully removed`() {

        val meal = meal()
        val mealPersister = MockMealPersister()
        val mealExtractor = MockMealExtractor(meal)

        val useCase = RemoveMealFromMenuUseCase(mealExtractor, mealPersister)
        val result = useCase.execute(meal.id)

        result shouldBeRight Unit
        mealPersister.verifyInvoked(meal)
        mealExtractor.verifyInvokedGetById(meal.id)
        mealPersister.verifyEventsAfterDeletion(meal.id)
    }

    @Test
    fun `meal not found`() {

        val mealPersister = MockMealPersister()
        val mealExtractor = MockMealExtractor()
        val useCase = RemoveMealFromMenuUseCase(mealExtractor, mealPersister)

        val mealId = mealId()
        val result = useCase.execute(mealId)

        result shouldBeLeft RemoveMealFromMenuUseCaseError.MealNotFound
        mealPersister.verifyEmpty()
        mealExtractor.verifyInvokedGetById(mealId)
    }
}