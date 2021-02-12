package com.stringconcat.ddd.order.domain.meal

import arrow.core.Either
import com.stringconcat.ddd.common.types.base.ValueObject
import java.math.BigDecimal

data class Price(val value: BigDecimal) : ValueObject {

    companion object {

        private const val SCALE = 2

        fun fromBigDecimal(price: BigDecimal): Either<CreatePriceError, Price> {

            return when {
                price.scale() > SCALE ->
                    Either.left(CreatePriceError.InvalidScale)

                price <= BigDecimal.ZERO ->
                    Either.left(CreatePriceError.NonPositiveValue)

                else -> Either.right(Price(price.setScale(SCALE)))
            }
        }
    }
}

sealed class CreatePriceError {
    object InvalidScale : CreatePriceError()
    object NonPositiveValue : CreatePriceError()
}