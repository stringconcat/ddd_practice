package com.stringconcat.ddd.order.usecase.providers

import com.stringconcat.ddd.order.domain.menu.MealId
import com.stringconcat.ddd.order.domain.menu.Price
import com.stringconcat.ddd.order.domain.providers.MealPriceProvider
import com.stringconcat.ddd.order.usecase.menu.MealExtractor

// можно сделать оптимизацию и загружать в юзкейсе сразу все
class MealPriceProviderImpl(private val extractor: MealExtractor) : MealPriceProvider {
    override fun price(mealId: MealId): Price {
        val meal = extractor.getById(mealId)
        check(meal != null) {
            "Meal #$mealId not found"
        }
        return meal.price
    }
}