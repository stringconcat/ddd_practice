package com.stringconcat.ddd.order.usecase.rules

import com.stringconcat.ddd.order.domain.menu.MealName
import com.stringconcat.ddd.order.domain.rules.MealAlreadyExistsRule
import com.stringconcat.ddd.order.usecase.menu.MealExtractor

class MealAlreadyExistsRuleImpl(val extractor: MealExtractor) : MealAlreadyExistsRule {
    override fun exists(name: MealName): Boolean {
        val meal = extractor.getByName(name)
        return meal != null && !meal.removed
    }
}