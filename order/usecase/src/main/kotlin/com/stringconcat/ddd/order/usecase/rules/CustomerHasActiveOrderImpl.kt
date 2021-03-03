package com.stringconcat.ddd.order.usecase.rules

import com.stringconcat.ddd.order.domain.cart.CustomerId
import com.stringconcat.ddd.order.domain.order.CustomerHasActiveOrder
import com.stringconcat.ddd.order.usecase.order.CustomerOrderExtractor

class CustomerHasActiveOrderImpl(
    private val customerOrderExtractor: CustomerOrderExtractor
) : CustomerHasActiveOrder {

    override fun check(forCustomer: CustomerId): Boolean {
        val lastOrder = customerOrderExtractor.getLastOrder(forCustomer)
        return lastOrder != null && lastOrder.isActive()
    }
}