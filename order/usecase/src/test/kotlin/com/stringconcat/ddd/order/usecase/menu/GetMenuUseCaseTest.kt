package com.stringconcat.ddd.order.usecase.menu

import com.stringconcat.ddd.order.usecase.TestMealExtractor
import com.stringconcat.ddd.order.usecase.meal
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import org.junit.jupiter.api.Test

class GetMenuUseCaseTest {

    private val mealExtractor = TestMealExtractor()

    @Test
    fun `get menu - menu is empty`() {
        val useCase = GetMenuUseCase(mealExtractor)
        val menu = useCase.getMenu()
        menu.shouldBeEmpty()
    }

    @Test
    fun `get menu`() {
        val meal = meal()
        mealExtractor[meal.id] = meal

        val useCase = GetMenuUseCase(mealExtractor)
        val menu = useCase.getMenu()
        menu shouldContainExactly listOf(
            MealInfo(
                id = meal.id.value,
                name = meal.name.value,
                description = meal.description.value,
                price = meal.price.value
            )
        )
    }
}