package com.stringconcat.ddd.order.usecase.menu

import com.stringconcat.ddd.order.domain.menu.Meal

interface MealPersister {
    fun save(meal: Meal)
}