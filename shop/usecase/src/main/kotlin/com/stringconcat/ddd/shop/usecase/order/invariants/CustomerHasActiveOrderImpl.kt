package com.stringconcat.ddd.shop.usecase.order.invariants

import com.stringconcat.ddd.shop.domain.cart.CustomerId
import com.stringconcat.ddd.shop.domain.order.CustomerHasActiveOrder
import com.stringconcat.ddd.shop.usecase.order.access.ShopOrderExtractor

class CustomerHasActiveOrderImpl(
    private val shopOrderExtractor: ShopOrderExtractor
) : CustomerHasActiveOrder {

    override fun invoke(forCustomer: CustomerId): Boolean {
        val lastOrder = shopOrderExtractor.getLastOrder(forCustomer)
        return lastOrder != null && lastOrder.isActive()
    }
}