package com.stringconcat.ddd.shop.usecase.order

import arrow.core.Either
import com.stringconcat.ddd.shop.domain.order.ShopOrderId
import com.stringconcat.ddd.shop.usecase.order.dto.OrderDetails

interface GetOrderById {
    fun execute(id: ShopOrderId): Either<GetOrderByIdUseCaseError, OrderDetails>
}

sealed class GetOrderByIdUseCaseError {
    object OrderNotFound : GetOrderByIdUseCaseError()
}