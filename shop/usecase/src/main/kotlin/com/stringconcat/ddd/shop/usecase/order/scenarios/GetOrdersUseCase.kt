package com.stringconcat.ddd.shop.usecase.order.scenarios

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.stringconcat.ddd.shop.domain.order.ShopOrderId
import com.stringconcat.ddd.shop.usecase.order.dto.OrderDetails
import com.stringconcat.ddd.shop.usecase.order.GetOrders
import com.stringconcat.ddd.shop.usecase.order.GetOrdersUseCaseError
import com.stringconcat.ddd.shop.usecase.order.access.ShopOrderExtractor
import com.stringconcat.ddd.shop.usecase.order.dto.toDetails

class GetOrdersUseCase(
    private val orderExtractor: ShopOrderExtractor,
    private val limit: () -> Int,
) : GetOrders {

    override fun execute(startId: ShopOrderId, limit: Int): Either<GetOrdersUseCaseError, List<OrderDetails>> {
        return if (limit() < limit) {
            GetOrdersUseCaseError.LimitExceed(limit()).left()
        } else {
            orderExtractor.getAll(startId, limit()).map {
                it.toDetails()
            }.toList().right()
        }
    }
}