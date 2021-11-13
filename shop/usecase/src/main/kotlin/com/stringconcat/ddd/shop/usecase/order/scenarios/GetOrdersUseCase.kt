package com.stringconcat.ddd.shop.usecase.order.scenarios

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.stringconcat.ddd.shop.domain.order.ShopOrderId
import com.stringconcat.ddd.shop.usecase.order.GetOrders
import com.stringconcat.ddd.shop.usecase.order.GetOrdersUseCaseError
import com.stringconcat.ddd.shop.usecase.order.ShopOrderInfo
import com.stringconcat.ddd.shop.usecase.order.access.ShopOrderExtractor

class GetOrdersUseCase(
    private val orderExtractor: ShopOrderExtractor,
    private val limit: () -> Int,
) : GetOrders {

    override fun execute(startId: ShopOrderId, limit: Int): Either<GetOrdersUseCaseError, List<ShopOrderInfo>> {
        return if (limit() < limit) {
            GetOrdersUseCaseError.LimitExceed(limit()).left()
        } else {
            orderExtractor.getAll(startId, limit()).map {
                ShopOrderInfo(
                    id = it.id,
                    state = it.state,
                    address = it.address,
                    total = it.totalPrice()
                )
            }.toList().right()
        }
    }
}