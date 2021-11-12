package com.stringconcat.ddd.shop.usecase.order.scenarios

import com.stringconcat.ddd.shop.domain.order.ShopOrderId
import com.stringconcat.ddd.shop.usecase.order.GetOrders
import com.stringconcat.ddd.shop.usecase.order.ShopOrderInfo
import com.stringconcat.ddd.shop.usecase.order.access.ShopOrderExtractor

class GetOrdersUseCase(
    private val orderExtractor: ShopOrderExtractor,
    private val limit: () -> Int,
) : GetOrders {

    override fun execute(startId: ShopOrderId): List<ShopOrderInfo> {
        return orderExtractor.getAll(startId, limit()).map {
            ShopOrderInfo(
                id = it.id,
                state = it.state,
                address = it.address,
                total = it.totalPrice()
            )
        }.toList()
    }
}