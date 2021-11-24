package com.stringconcat.ddd.shop.persistence.order

import com.stringconcat.ddd.common.events.DomainEventPublisher
import com.stringconcat.ddd.shop.domain.cart.CustomerId
import com.stringconcat.ddd.shop.domain.order.ShopOrder
import com.stringconcat.ddd.shop.domain.order.ShopOrderId
import com.stringconcat.ddd.shop.usecase.order.access.ShopOrderExtractor
import com.stringconcat.ddd.shop.usecase.order.access.ShopOrderPersister
import java.util.TreeMap

class InMemoryShopOrderRepository(private val eventPublisher: DomainEventPublisher) :
    ShopOrderExtractor,
    ShopOrderPersister {

    internal val storage = TreeMap<ShopOrderId, ShopOrder> { k1, k2 -> k1.toLongValue().compareTo(k2.toLongValue()) }

    override fun getById(orderId: ShopOrderId) = storage[orderId]

    override fun getLastOrder(forCustomer: CustomerId) =
        storage.values
            .filter { it.forCustomer == forCustomer }
            .toSortedSet { o1, o2 -> o1.created.compareTo(o2.created) }
            .lastOrNull()

    override fun getAll(startId: ShopOrderId, limit: Int) =
        storage.tailMap(startId).toList().take(limit).map { it.second }

    override fun save(order: ShopOrder) {
        eventPublisher.publish(order.popEvents())
        storage[order.id] = order
    }
}