package com.stringconcat.ddd.shop.domain.menu

fun interface MealAlreadyExists {
    operator fun invoke(name: MealName): Boolean
}