package com.stringconcat.ddd.order.domain.menu

interface MealAlreadyExists {
    fun check(name: MealName): Boolean
}