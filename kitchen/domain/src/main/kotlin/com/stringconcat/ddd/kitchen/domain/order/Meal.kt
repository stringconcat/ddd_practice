package com.stringconcat.ddd.kitchen.domain.order

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.stringconcat.ddd.common.types.base.ValueObject

data class Meal internal constructor(val value: String) : ValueObject {

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
