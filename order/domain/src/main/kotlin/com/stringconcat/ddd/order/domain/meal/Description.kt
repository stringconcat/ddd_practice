package com.stringconcat.ddd.order.domain.meal

import arrow.core.Either
import com.stringconcat.ddd.common.types.base.ValueObject

data class Description internal constructor(val value: String) : ValueObject {

    companion object {

        fun fromString(description: String): Either<CreateDescriptionError, Description> {
            return if (description.isNotBlank()) {
                Either.right(Description(description))
            } else {
                Either.left(CreateDescriptionError.EmptyString)
            }
        }
    }
}

sealed class CreateDescriptionError {
    object EmptyString : CreateDescriptionError()
}
