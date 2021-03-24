package com.stringconcat.ddd.order.usecase.order

import arrow.core.Either
import com.stringconcat.ddd.order.domain.cart.CustomerId
import com.stringconcat.ddd.order.domain.order.OrderState

interface GetLastOrderState {
    fun execute(forCustomer: CustomerId): Either<GetLastOrderStateUseCaseError, OrderState>
}

sealed class GetLastOrderStateUseCaseError(val message: String) {
    object OrderNotFound : GetLastOrderStateUseCaseError("Order not found")
}