package com.stringconcat.ddd.shop.rest.menu

import arrow.core.ValidatedNel
import com.stringconcat.ddd.shop.domain.menu.CreateMealDescriptionError
import com.stringconcat.ddd.shop.domain.menu.CreateMealNameError
import com.stringconcat.ddd.shop.domain.menu.CreatePriceError
import com.stringconcat.ddd.shop.domain.menu.MealDescription
import com.stringconcat.ddd.shop.domain.menu.MealName
import com.stringconcat.ddd.shop.domain.menu.Price
import java.math.BigDecimal

fun MealName.Companion.validated(name: String): ValidatedNel<ValidationError, MealName> {
    return from(name).mapLeft {
        when (it) {
            is CreateMealNameError.EmptyMealNameError,
            -> ValidationError(message = "Meal name is empty")
        }
    }.toValidatedNel()
}

fun MealDescription.Companion.validated(
    description: String
): ValidatedNel<ValidationError, MealDescription> {
    return from(description).mapLeft {
        when (it) {
            is CreateMealDescriptionError.EmptyDescriptionError,
            -> ValidationError(message = "Meal description is empty")
        }
    }.toValidatedNel()
}

fun Price.Companion.validated(price: BigDecimal): ValidatedNel<ValidationError, Price> {
    return from(price).mapLeft {
        when (it) {
            is CreatePriceError.InvalidScale,
            -> ValidationError(message = "Price scale must not be > 2")
            is CreatePriceError.NegativeValue,
            -> ValidationError(message = "Price must not be negative")
        }
    }.toValidatedNel()
}