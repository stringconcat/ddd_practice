package com.stringconcat.ddd.order.domain.menu

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.stringconcat.ddd.common.types.base.ValueObject

data class MealName internal constructor(val value: String) : ValueObject {

    companion object {

        fun from(name: String): Either<EmptyMealNameError, MealName> =
            if (name.isNotBlank()) {
                MealName(name).right()
            } else {
                EmptyMealNameError.left()
            }
    }
}

object EmptyMealNameError