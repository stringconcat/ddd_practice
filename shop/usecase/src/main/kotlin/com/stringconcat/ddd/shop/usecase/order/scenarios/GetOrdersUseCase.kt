package com.stringconcat.ddd.shop.usecase.order.scenarios

import com.stringconcat.ddd.shop.usecase.order.GetOrders
import com.stringconcat.ddd.shop.usecase.order.ShopOrderInfo
import com.stringconcat.ddd.shop.usecase.order.access.ShopOrderExtractor

class GetOrdersUseCase(private val orderExtractor: ShopOrderExtractor) : GetOrders {
    override fun execute(): List<ShopOrderInfo> {
        return orderExtractor.getAll().map {
            ShopOrderInfo(
                id = it.id,
                state = it.state,
                address = it.address,
                total = it.totalPrice()
            )
        }.toList()
    }
}