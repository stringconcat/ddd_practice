package com.stringconcat.ddd.shop.usecase.menu.scenarios

import com.stringconcat.ddd.shop.domain.meal
import com.stringconcat.ddd.shop.domain.mealId
import com.stringconcat.ddd.shop.usecase.TestMealExtractor
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
        val mealExtractor = TestMealExtractor()
        val useCase = GetMealByIdUseCase(mealExtractor)
        val result = useCase.execute(mealId())
        result shouldBeLeft GetMealByIdUseCaseError.MealNotFound
    }

    @Test
    fun `meal removed`() {
        val meal = removedMeal()
        val mealExtractor = TestMealExtractor()
        mealExtractor[meal.id] = meal
        val useCase = GetMealByIdUseCase(mealExtractor)

        val result = useCase.execute(meal.id)
        result shouldBeLeft GetMealByIdUseCaseError.MealNotFound
    }

    @Test
    fun `meal extracted successfully`() {
        val meal = meal()
        val mealExtractor = TestMealExtractor()
        mealExtractor[meal.id] = meal
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
    }
}