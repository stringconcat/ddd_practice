package com.stringconcat.ddd.shop.usecase.rules

import com.stringconcat.ddd.shop.domain.menu.MealName
import com.stringconcat.ddd.shop.domain.menu.MealAlreadyExists
import com.stringconcat.ddd.shop.usecase.menu.scenarios.MealExtractor

class MealAlreadyExistsImpl(val extractor: MealExtractor) : MealAlreadyExists {
    override fun check(name: MealName): Boolean {
        val meal = extractor.getByName(name)
        return meal != null && !meal.removed
    }
}