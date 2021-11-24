package com.stringconcat.ddd.kitchen.domain.order

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.stringconcat.ddd.common.types.base.ValueObject
import com.stringconcat.ddd.common.types.error.BusinessError

data class Meal internal constructor(private val value: String) : ValueObject {

    fun toStringValue() = value

    companion object {
        fun from(name: String): Either<EmptyMealNameError, Meal> =
            if (name.isNotBlank()) {
                Meal(name).right()
            } else {
                EmptyMealNameError.left()
            }
    }
}

object EmptyMealNameError : BusinessError
