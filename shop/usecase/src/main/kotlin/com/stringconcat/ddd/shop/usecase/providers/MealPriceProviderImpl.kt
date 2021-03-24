package com.stringconcat.ddd.shop.usecase.providers

import com.stringconcat.ddd.order.domain.menu.MealId
import com.stringconcat.ddd.order.domain.menu.Price
import com.stringconcat.ddd.order.domain.order.MealPriceProvider
import com.stringconcat.ddd.shop.usecase.menu.MealExtractor

// можно сделать оптимизацию и загружать в юзкейсе сразу все
class MealPriceProviderImpl(private val extractor: MealExtractor) : MealPriceProvider {
    override fun getPrice(forMealId: MealId): Price {
        val meal = extractor.getById(forMealId)
        check(meal != null) {
            "Meal #$forMealId not found"
        }
        return meal.price
    }
}