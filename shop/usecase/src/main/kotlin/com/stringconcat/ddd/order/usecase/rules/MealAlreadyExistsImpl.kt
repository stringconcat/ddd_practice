package com.stringconcat.ddd.order.usecase.rules

import com.stringconcat.ddd.order.domain.menu.MealName
import com.stringconcat.ddd.order.domain.menu.MealAlreadyExists
import com.stringconcat.ddd.order.usecase.menu.MealExtractor

class MealAlreadyExistsImpl(val extractor: MealExtractor) : MealAlreadyExists {
    override fun check(name: MealName): Boolean {
        val meal = extractor.getByName(name)
        return meal != null && !meal.removed
    }
}