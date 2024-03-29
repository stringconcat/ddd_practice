package com.stringconcat.ddd.shop.usecase.menu.scenarios

import com.stringconcat.ddd.shop.domain.meal
import com.stringconcat.ddd.shop.usecase.MockMealExtractor
import com.stringconcat.ddd.shop.usecase.menu.dto.MealInfo
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import org.junit.jupiter.api.Test

class GetMenuUseCaseTest {

    @Test
    fun `get menu - menu is empty`() {
        val mealExtractor = MockMealExtractor()
        val useCase = GetMenuUseCase(mealExtractor)
        val menu = useCase.execute()

        menu.shouldBeEmpty()
        mealExtractor.verifyInvokedGetAll()
    }

    @Test
    fun `get menu`() {
        val meal = meal()
        val mealExtractor = MockMealExtractor(meal)

        val useCase = GetMenuUseCase(mealExtractor)
        val menu = useCase.execute()

        menu shouldContainExactly listOf(
            MealInfo(
                id = meal.id,
                name = meal.name,
                description = meal.description,
                price = meal.price,
                version = meal.version
            )
        )
        mealExtractor.verifyInvokedGetAll()
    }
}