package com.stringconcat.ddd.shop.usecase.menu

import arrow.core.Either
import arrow.core.zip
import com.stringconcat.ddd.shop.domain.menu.CreatePriceError
import com.stringconcat.ddd.shop.domain.menu.EmptyDescriptionError
import com.stringconcat.ddd.shop.domain.menu.EmptyMealNameError
import com.stringconcat.ddd.shop.domain.menu.MealDescription
import com.stringconcat.ddd.shop.domain.menu.MealId
import com.stringconcat.ddd.shop.domain.menu.MealName
import com.stringconcat.ddd.shop.domain.menu.Price
import java.math.BigDecimal

interface AddMealToMenu {
    fun execute(request: AddMealToMenuRequest): Either<AddMealToMenuUseCaseError, MealId>
}

sealed class AddMealToMenuUseCaseError(open val message: String) {
    object AlreadyExists : AddMealToMenuUseCaseError("Meal already exists")
}

data class AddMealToMenuRequest internal constructor(
    val name: MealName,
    val description: MealDescription,
    val price: Price
) {

    companion object {

        fun from(
            name: String,
            description: String,
            price: BigDecimal
        ): Either<InvalidMealParameters, AddMealToMenuRequest> {
            return MealName.from(name).mapLeft { it.toError() }
                .zip(
                    MealDescription.from(description).mapLeft { it.toError() },
                    Price.from(price).mapLeft { it.toError() }
                ) { mealName, mealDescription, mealPrice ->
                    AddMealToMenuRequest(mealName, mealDescription, mealPrice)
                }
        }
    }
}

data class InvalidMealParameters(val message: String)

fun EmptyMealNameError.toError() = InvalidMealParameters("Empty name")
fun EmptyDescriptionError.toError() = InvalidMealParameters("Empty description")

fun CreatePriceError.toError(): InvalidMealParameters {
    return when (this) {
        is CreatePriceError.InvalidScale -> InvalidMealParameters("Invalid scale")
        is CreatePriceError.NegativeValue -> InvalidMealParameters("Negative value")
    }
}