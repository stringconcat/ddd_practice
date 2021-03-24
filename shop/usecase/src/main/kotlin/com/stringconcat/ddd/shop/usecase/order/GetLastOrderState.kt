package com.stringconcat.ddd.shop.usecase.order

import arrow.core.Either
import com.stringconcat.ddd.shop.domain.cart.CustomerId
import com.stringconcat.ddd.shop.domain.order.OrderState

interface GetLastOrderState {
    fun execute(forCustomer: CustomerId): Either<GetLastOrderStateUseCaseError, OrderState>
}

sealed class GetLastOrderStateUseCaseError(val message: String) {
    object OrderNotFound : GetLastOrderStateUseCaseError("Order not found")
}