package com.stringconcat.ddd.order.domain.order

import com.stringconcat.ddd.order.domain.menu.MealId
import com.stringconcat.ddd.order.domain.menu.Price

interface MealPriceProvider {
    fun getPrice(forMealId: MealId): Price
}