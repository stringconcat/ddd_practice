package com.stringconcat.ddd.shop.usecase.order.providers

import com.stringconcat.ddd.shop.domain.meal
import com.stringconcat.ddd.shop.domain.mealId
import com.stringconcat.ddd.shop.usecase.MockMealExtractor
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class MealPriceProviderImplTest {

    @Test
    fun `price has been provided`() {
        val meal = meal()

        val extractor = MockMealExtractor(meal)

        val provider = MealPriceProviderImpl(extractor)
        val result = provider.getPrice(meal.id)

        extractor.verifyInvokedGetById(meal.id)
        result shouldBe meal.price
    }

    @Test
    fun `meal not found`() {
        val extractor = MockMealExtractor()
        val provider = MealPriceProviderImpl(extractor)

        val mealId = mealId()

        shouldThrow<IllegalStateException> {
            provider.getPrice(mealId)
        }
        extractor.verifyInvokedGetById(mealId)
    }
}
