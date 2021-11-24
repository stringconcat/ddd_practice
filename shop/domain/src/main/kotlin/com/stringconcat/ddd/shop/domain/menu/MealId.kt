package com.stringconcat.ddd.shop.domain.menu

data class MealId(private val value: Long) {
    fun toLongValue() = value
}

interface MealIdGenerator {
    fun generate(): MealId
}