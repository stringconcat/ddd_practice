package com.stringconcat.ddd.shop.usecase.order.scenarios

import arrow.core.rightIfNotNull
import com.stringconcat.ddd.shop.domain.order.ShopOrderId
import com.stringconcat.ddd.shop.usecase.order.GetOrderById
import com.stringconcat.ddd.shop.usecase.order.GetOrderByIdUseCaseError
import com.stringconcat.ddd.shop.usecase.order.OrderDetails
import com.stringconcat.ddd.shop.usecase.order.OrderItemDetails
import com.stringconcat.ddd.shop.usecase.order.access.ShopOrderExtractor

class GetOrderByIdUseCase(private val orderExtractor: ShopOrderExtractor) : GetOrderById {
    override fun execute(id: ShopOrderId) =
        orderExtractor.getById(id)
            .rightIfNotNull { GetOrderByIdUseCaseError.OrderNotFound }
            .map { order ->
                val items =
                    order.orderItems
                        .map { OrderItemDetails(mealId = it.mealId, count = it.count) }
                OrderDetails(
                    id = order.id,
                    items = items,
                    total = order.totalPrice(),
                    state = order.state,
                    address = order.address
                )
            }
}
