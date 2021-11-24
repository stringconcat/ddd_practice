package com.stringconcat.ddd.common.types.common

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.stringconcat.ddd.common.types.base.ValueObject

data class Address internal constructor(
    private val street: String,
    private val building: Int,
) : ValueObject {

    fun streetToStringValue() = street

    fun buildingToIntValue() = building

    companion object {

        // тут может быть валидация по КЛАДР
        fun from(street: String, building: Int): Either<CreateAddressError, Address> {
            return when {
                street.isBlank() -> CreateAddressError.EmptyString.left()
                building <= 0 -> CreateAddressError.NonPositiveBuilding.left()
                else -> Address(street, building).right()
            }
        }
    }
}

sealed class CreateAddressError {
    object EmptyString : CreateAddressError()
    object NonPositiveBuilding : CreateAddressError()
}