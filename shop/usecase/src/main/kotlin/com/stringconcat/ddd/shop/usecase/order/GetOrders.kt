package com.stringconcat.ddd.shop.usecase.order

import arrow.core.Either
import com.stringconcat.ddd.common.types.common.Address
import com.stringconcat.ddd.shop.domain.menu.Price
import com.stringconcat.ddd.shop.domain.order.ShopOrderId
import com.stringconcat.ddd.shop.domain.order.OrderState

interface GetOrders {
    fun execute(startId: ShopOrderId, limit: Int): Either<GetOrdersUseCaseError, List<ShopOrderInfo>>
}

sealed class GetOrdersUseCaseError {
    data class LimitExceed(val maxSize: Int) : GetOrdersUseCaseError()
}

data class ShopOrderInfo(
    val id: ShopOrderId,
    val state: OrderState,
    val address: Address,
    val total: Price,
)