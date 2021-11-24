package com.stringconcat.ddd.shop.usecase.menu.scenarios

import com.stringconcat.ddd.shop.domain.meal
import com.stringconcat.ddd.shop.domain.mealId
import com.stringconcat.ddd.shop.usecase.MockMealExtractor
import com.stringconcat.ddd.shop.usecase.menu.GetMealByIdUseCaseError
import com.stringconcat.ddd.shop.usecase.menu.dto.MealInfo
import com.stringconcat.ddd.shop.usecase.removedMeal
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class GetMealByIdUseCaseTest {

    @Test
    fun `meal not found`() {
        val mealExtractor = MockMealExtractor()
        val useCase = GetMealByIdUseCase(mealExtractor)

        val mealId = mealId()
        val result = useCase.execute(mealId)

        result shouldBeLeft GetMealByIdUseCaseError.MealNotFound
        mealExtractor.verifyInvokedGetById(mealId)
    }

    @Test
    fun `meal removed`() {
        val meal = removedMeal()
        val mealExtractor = MockMealExtractor(meal)
        val useCase = GetMealByIdUseCase(mealExtractor)

        val result = useCase.execute(meal.id)

        result shouldBeLeft GetMealByIdUseCaseError.MealNotFound
        mealExtractor.verifyInvokedGetById(meal.id)
    }

    @Test
    fun `meal extracted successfully`() {
        val meal = meal()
        val mealExtractor = MockMealExtractor(meal)
        val useCase = GetMealByIdUseCase(mealExtractor)

        val result = useCase.execute(meal.id)
        val mealInfo = result.shouldBeRight()

        mealInfo shouldBe MealInfo(
            id = meal.id,
            name = meal.name,
            description = meal.description,
            price = meal.price,
            version = meal.version
        )
        mealExtractor.verifyInvokedGetById(meal.id)
    }
}