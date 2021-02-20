package com.stringconcat.ddd.order.usecase.rules

import com.stringconcat.ddd.order.domain.cart.CustomerId
import com.stringconcat.ddd.order.domain.rules.CustomerHasActiveOrderRule
import com.stringconcat.ddd.order.usecase.order.CustomerOrderExtractor

class CustomerHasActiveOrderRuleImpl(
    private val customerOrderExtractor: CustomerOrderExtractor
) : CustomerHasActiveOrderRule {

    override fun hasActiveOrder(customerId: CustomerId): Boolean {
        val lastOrder = customerOrderExtractor.getLastOrder(customerId)
        return lastOrder != null && lastOrder.isActive()
    }
}