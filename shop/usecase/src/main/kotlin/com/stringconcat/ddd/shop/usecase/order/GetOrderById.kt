package com.stringconcat.ddd.shop.usecase.order

import arrow.core.Either
import com.stringconcat.ddd.common.types.common.Address
import com.stringconcat.ddd.common.types.common.Count
import com.stringconcat.ddd.shop.domain.menu.MealId
import com.stringconcat.ddd.shop.domain.menu.Price
import com.stringconcat.ddd.shop.domain.order.OrderState
import com.stringconcat.ddd.shop.domain.order.ShopOrderId

interface GetOrderById {
    fun execute(id: ShopOrderId): Either<GetOrderByIdUseCaseError, OrderDetails>
}

sealed class GetOrderByIdUseCaseError {
    object OrderNotFound : GetOrderByIdUseCaseError()
}

data class OrderDetails(
    val id: ShopOrderId,
    val state: OrderState,
    val address: Address,
    val items: List<OrderItemDetails>,
    val total: Price,
)

data class OrderItemDetails(
    val mealId: MealId,
    val count: Count
)