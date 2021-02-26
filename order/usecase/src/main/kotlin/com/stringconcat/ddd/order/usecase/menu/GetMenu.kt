package com.stringconcat.ddd.order.usecase.menu

interface GetMenu {
    fun execute(): List<MealInfo>
}