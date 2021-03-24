package com.stringconcat.ddd.shop.usecase.rules

import com.stringconcat.ddd.shop.domain.cart.CustomerId
import com.stringconcat.ddd.shop.domain.order.CustomerHasActiveOrder
import com.stringconcat.ddd.shop.usecase.order.CustomerOrderExtractor

class CustomerHasActiveOrderImpl(
    private val customerOrderExtractor: CustomerOrderExtractor
) : CustomerHasActiveOrder {

    override fun check(forCustomer: CustomerId): Boolean {
        val lastOrder = customerOrderExtractor.getLastOrder(forCustomer)
        return lastOrder != null && lastOrder.isActive()
    }
}