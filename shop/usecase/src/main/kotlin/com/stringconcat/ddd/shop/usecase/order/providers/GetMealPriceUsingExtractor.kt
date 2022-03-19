package com.stringconcat.ddd.shop.usecase.order.providers

import com.stringconcat.ddd.shop.domain.menu.MealId
import com.stringconcat.ddd.shop.domain.menu.Price
import com.stringconcat.ddd.shop.domain.order.GetMealPrice
import com.stringconcat.ddd.shop.usecase.menu.access.MealExtractor

// можно сделать оптимизацию и загружать в юзкейсе сразу все
class GetMealPriceUsingExtractor(private val extractor: MealExtractor) : GetMealPrice {
    override fun invoke(forMealId: MealId): Price {
        val meal = extractor.getById(forMealId)
        check(meal != null) {
            "Meal #$forMealId not found"
        }
        return meal.price
    }
}