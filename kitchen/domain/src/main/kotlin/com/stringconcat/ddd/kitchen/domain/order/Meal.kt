package com.stringconcat.ddd.kitchen.domain.order

import arrow.core.Either
import arrow.core.left
import arrow.core.right

data class Meal(val value: String) {

    companion object {
        fun from(name: String): Either<EmptyMealNameError, Meal> {
            return if (name.isNotBlank()) {
                Meal(name).right()
            } else {
                EmptyMealNameError.left()
            }
        }
    }
}

object EmptyMealNameError
