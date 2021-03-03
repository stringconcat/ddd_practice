package com.stringconcat.ddd.order.usecase.rules

import com.stringconcat.ddd.order.usecase.TestMealExtractor
import com.stringconcat.ddd.order.usecase.meal
import com.stringconcat.ddd.order.usecase.mealName
import com.stringconcat.ddd.order.usecase.removedMeal
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import org.junit.jupiter.api.Test

internal class MealAlreadyExistsImplTest {

    @Test
    fun `meal already exists`() {
        val meal = meal()
        val extractor = TestMealExtractor().apply {
            this[meal.id] = meal
        }
        val rule = MealAlreadyExistsImpl(extractor)

        val result = rule.check(meal.name)
        result.shouldBeTrue()
    }

    @Test
    fun `meal already exists but removed`() {
        val meal = removedMeal()
        val extractor = TestMealExtractor().apply {
            this[meal.id] = meal
        }
        val rule = MealAlreadyExistsImpl(extractor)

        val result = rule.check(meal.name)
        result.shouldBeFalse()
    }

    @Test
    fun `meal already exists doesn't exist`() {
        val extractor = TestMealExtractor()
        val rule = MealAlreadyExistsImpl(extractor)
        val result = rule.check(mealName())
        result.shouldBeFalse()
    }
}