package com.stringconcat.ddd.shop.domain.menu

interface MealAlreadyExists {
    fun check(name: MealName): Boolean
}