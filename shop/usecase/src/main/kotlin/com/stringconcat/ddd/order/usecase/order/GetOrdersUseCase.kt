package com.stringconcat.ddd.order.usecase.order

class GetOrdersUseCase(private val orderExtractor: CustomerOrderExtractor) : GetOrders {
    override fun execute(): List<CustomerOrderInfo> {
        return orderExtractor.getAll().map {
            CustomerOrderInfo(
                id = it.id,
                state = it.state,
                address = it.address,
                total = it.totalPrice()
            )
        }.toList()
    }
}