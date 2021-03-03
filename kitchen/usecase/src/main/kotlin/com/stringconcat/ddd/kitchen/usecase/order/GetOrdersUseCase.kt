package com.stringconcat.ddd.kitchen.usecase.order

class GetOrdersUseCase(private val orderExtractor: KitchenOrderExtractor) : GetOrders {

    override fun execute(): List<KitchenOrderInfo> {
        return orderExtractor.getAll().map {
            KitchenOrderInfo(
                id = it.id,
                meals = it.meals,
                cooked = it.cooked
            )
        }.toList()
    }
}