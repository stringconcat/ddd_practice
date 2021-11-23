package com.stringconcat.ddd.shop.usecase.menu.invariants

import com.stringconcat.ddd.shop.domain.meal
import com.stringconcat.ddd.shop.domain.mealName
import com.stringconcat.ddd.shop.usecase.MockMealExtractor
import com.stringconcat.ddd.shop.usecase.removedMeal
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import org.junit.jupiter.api.Test

internal class MealAlreadyExistsImplTest {

    @Test
    fun `meal already exists`() {
        val meal = meal()
        val extractor = MockMealExtractor(meal)
        val rule = MealAlreadyExistsImpl(extractor)

        val result = rule.check(meal.name)

        result.shouldBeTrue()
        extractor.verifyInvokedGetByName(meal.name)
    }

    @Test
    fun `meal already exists but removed`() {
        val meal = removedMeal()
        val extractor = MockMealExtractor(meal)
        val rule = MealAlreadyExistsImpl(extractor)

        val result = rule.check(meal.name)

        result.shouldBeFalse()
        extractor.verifyInvokedGetByName(meal.name)
    }

    @Test
    fun `meal already exists doesn't exist`() {
        val extractor = MockMealExtractor()
        val rule = MealAlreadyExistsImpl(extractor)

        val mealName = mealName()
        val result = rule.check(mealName)

        result.shouldBeFalse()
        extractor.verifyInvokedGetByName(mealName)
    }
}