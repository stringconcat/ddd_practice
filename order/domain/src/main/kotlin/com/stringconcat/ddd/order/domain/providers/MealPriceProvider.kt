package com.stringconcat.ddd.order.domain.providers

import com.stringconcat.ddd.order.domain.menu.MealId
import com.stringconcat.ddd.order.domain.menu.Price

interface MealPriceProvider {
    fun price(mealId: MealId): Price
}