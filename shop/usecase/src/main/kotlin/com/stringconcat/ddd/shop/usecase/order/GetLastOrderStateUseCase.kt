package com.stringconcat.ddd.shop.usecase.order

import arrow.core.Either
import com.stringconcat.ddd.order.domain.cart.CustomerId
import com.stringconcat.ddd.order.domain.order.OrderState

class GetLastOrderStateUseCase(private val orderExtractor: CustomerOrderExtractor) : GetLastOrderState {

    override fun execute(forCustomer: CustomerId): Either<GetLastOrderStateUseCaseError, OrderState> {
        return orderExtractor.getLastOrder(forCustomer)
            .rightIfNotNull { GetLastOrderStateUseCaseError.OrderNotFound }
            .map { it.state }
    }
}