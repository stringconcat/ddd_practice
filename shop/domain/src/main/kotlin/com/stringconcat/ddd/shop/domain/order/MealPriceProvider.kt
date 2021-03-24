package com.stringconcat.ddd.shop.domain.order

import com.stringconcat.ddd.shop.domain.menu.MealId
import com.stringconcat.ddd.shop.domain.menu.Price

interface MealPriceProvider {
    fun getPrice(forMealId: MealId): Price
}