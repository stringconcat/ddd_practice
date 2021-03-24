package com.stringconcat.ddd.shop.usecase.order

import arrow.core.Either
import arrow.core.rightIfNotNull
import com.stringconcat.ddd.shop.domain.cart.CustomerId
import com.stringconcat.ddd.shop.domain.order.OrderState

class GetLastOrderStateUseCase(private val orderExtractor: ShopOrderExtractor) : GetLastOrderState {

    override fun execute(forCustomer: CustomerId): Either<GetLastOrderStateUseCaseError, OrderState> {
        return orderExtractor.getLastOrder(forCustomer)
            .rightIfNotNull { GetLastOrderStateUseCaseError.OrderNotFound }
            .map { it.state }
    }
}