package com.stringconcat.ddd.shop.usecase.menu

import com.stringconcat.ddd.order.domain.menu.Meal
import com.stringconcat.ddd.order.domain.menu.MealId
import com.stringconcat.ddd.order.domain.menu.MealName

interface MealExtractor {

    fun getById(id: MealId): Meal?

    fun getByName(name: MealName): Meal?

    fun getAll(): List<Meal>
}