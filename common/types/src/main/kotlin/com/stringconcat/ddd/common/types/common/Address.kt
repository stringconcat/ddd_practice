package com.stringconcat.ddd.common.types.common

import arrow.core.Either

data class Address internal constructor(val street: String, val building: Int) {

    companion object {

        // тут может быть валидация по КЛАДР
        fun from(street: String, building: Int): Either<CreateAddressError, Address> {
            return when {
                street.isBlank() -> Either.left(CreateAddressError.EmptyString)
                building <= 0 -> Either.left(CreateAddressError.NonPositiveBuilding)
                else -> Either.right(Address(street, building))
            }
        }
    }
}

sealed class CreateAddressError {
    object EmptyString : CreateAddressError()
    object NonPositiveBuilding : CreateAddressError()
}