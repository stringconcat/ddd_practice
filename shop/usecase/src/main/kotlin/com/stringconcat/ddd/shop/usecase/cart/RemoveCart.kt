package com.stringconcat.ddd.shop.usecase.cart

import arrow.core.Either
import com.stringconcat.ddd.order.domain.cart.CustomerId

interface RemoveCart {
    fun execute(forCustomer: CustomerId): Either<RemoveCartHandlerError, Unit>
}

sealed class RemoveCartHandlerError {
    object CartNotFound : RemoveCartHandlerError()
}