package com.stringconcat.ddd.shop.usecase.menu

import arrow.core.Either
import com.stringconcat.ddd.shop.domain.menu.MealDescription
import com.stringconcat.ddd.shop.domain.menu.MealId
import com.stringconcat.ddd.shop.domain.menu.MealName
import com.stringconcat.ddd.shop.domain.menu.Price

interface AddMealToMenu {
    fun execute(name: MealName, description: MealDescription, price: Price): Either<AddMealToMenuUseCaseError, MealId>
}

sealed class AddMealToMenuUseCaseError {
    object AlreadyExists : AddMealToMenuUseCaseError()
}