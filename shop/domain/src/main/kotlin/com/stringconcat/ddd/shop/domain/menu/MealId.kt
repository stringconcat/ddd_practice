package com.stringconcat.ddd.shop.domain.menu

data class MealId(val value: Long)

interface MealIdGenerator {
    fun generate(): MealId
}