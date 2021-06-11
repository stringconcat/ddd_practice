package com.stringconcat.ddd.shop.query.order

class GetOrdersUseCase(private val queryHandler: ShopOrderQueryHandler) : GetOrders {
    override fun execute(): List<ShopOrderInfo> {
        return queryHandler.getAll()
    }
}