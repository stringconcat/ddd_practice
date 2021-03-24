package com.stringconcat.ddd.shop.usecase.menu

import arrow.core.Either
import arrow.core.extensions.either.apply.tupled
import com.stringconcat.ddd.order.domain.menu.CreatePriceError
import com.stringconcat.ddd.order.domain.menu.EmptyDescriptionError
import com.stringconcat.ddd.order.domain.menu.EmptyMealNameError
import com.stringconcat.ddd.order.domain.menu.MealDescription
import com.stringconcat.ddd.order.domain.menu.MealName
import com.stringconcat.ddd.order.domain.menu.Price
import java.math.BigDecimal

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
            return tupled(
                MealName.from(name).mapLeft { it.toError() },
                MealDescription.from(description).mapLeft { it.toError() },
                Price.from(price).mapLeft { it.toError() }
            ).map { params ->
                AddMealToMenuRequest(params.a, params.b, params.c)
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