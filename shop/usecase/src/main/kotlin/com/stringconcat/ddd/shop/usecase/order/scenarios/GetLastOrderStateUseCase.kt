package com.stringconcat.ddd.shop.usecase.order.scenarios

import arrow.core.Either
import arrow.core.rightIfNotNull
import com.stringconcat.ddd.shop.domain.cart.CustomerId
import com.stringconcat.ddd.shop.domain.order.OrderState
import com.stringconcat.ddd.shop.usecase.order.GetLastOrderState
import com.stringconcat.ddd.shop.usecase.order.GetLastOrderStateUseCaseError
import com.stringconcat.ddd.shop.usecase.order.access.ShopOrderExtractor

class GetLastOrderStateUseCase(private val orderExtractor: ShopOrderExtractor) : GetLastOrderState {

    override fun execute(forCustomer: CustomerId): Either<GetLastOrderStateUseCaseError, OrderState> {
        return orderExtractor.getLastOrder(forCustomer)
            .rightIfNotNull { GetLastOrderStateUseCaseError.OrderNotFound }
            .map { it.state }
    }
}