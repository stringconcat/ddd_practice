package com.stringconcat.ddd.order.usecase.menu

import com.stringconcat.ddd.order.domain.menu.Meal
import com.stringconcat.ddd.order.domain.menu.MealId

interface MealExtractor {
    fun getById(id: MealId): Meal?
}