package com.stringconcat.ddd.shop.usecase.order

import arrow.core.Either
import com.stringconcat.ddd.shop.domain.order.ShopOrderId
import com.stringconcat.ddd.shop.usecase.order.dto.OrderDetails

interface GetOrders {
    fun execute(startId: ShopOrderId, limit: Int): Either<GetOrdersUseCaseError, List<OrderDetails>>
}

sealed class GetOrdersUseCaseError {
    data class LimitExceed(val maxSize: Int) : GetOrdersUseCaseError()
}