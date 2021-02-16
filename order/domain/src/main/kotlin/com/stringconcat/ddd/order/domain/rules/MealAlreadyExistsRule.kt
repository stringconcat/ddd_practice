package com.stringconcat.ddd.order.domain.rules

import com.stringconcat.ddd.order.domain.menu.MealName

interface MealAlreadyExistsRule {
    fun exists(name: MealName): Boolean
}