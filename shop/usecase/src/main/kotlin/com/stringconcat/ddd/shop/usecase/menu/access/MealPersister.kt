package com.stringconcat.ddd.shop.usecase.menu.access

import com.stringconcat.ddd.shop.domain.menu.Meal

interface MealPersister {
    fun save(meal: Meal)
}