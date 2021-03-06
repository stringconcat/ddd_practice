package com.stringconcat.ddd.shop.persistence.order

import com.stringconcat.ddd.common.types.base.EventPublisher
import com.stringconcat.ddd.shop.domain.cart.CustomerId
import com.stringconcat.ddd.shop.domain.order.ShopOrder
import com.stringconcat.ddd.shop.domain.order.ShopOrderId
import com.stringconcat.ddd.shop.usecase.order.ShopOrderExtractor
import com.stringconcat.ddd.shop.usecase.order.ShopOrderPersister

class InMemoryShopOrderRepository(private val eventPublisher: EventPublisher) :
    ShopOrderExtractor,
    ShopOrderPersister {

    internal val storage = LinkedHashMap<ShopOrderId, ShopOrder>()

    override fun getById(orderId: ShopOrderId) = storage[orderId]

    override fun getLastOrder(forCustomer: CustomerId) =
        storage.filter { it.component2().forCustomer == forCustomer }.values.lastOrNull()

    override fun save(order: ShopOrder) {
        eventPublisher.publish(order.popEvents())
        storage[order.id] = order
    }

    override fun getAll(): List<ShopOrder> = storage.values.toList()
}