package com.stringconcat.ddd.order.domain.menu

data class MealId(val value: Long)

interface MealIdGenerator {
    fun generate(): MealId
}