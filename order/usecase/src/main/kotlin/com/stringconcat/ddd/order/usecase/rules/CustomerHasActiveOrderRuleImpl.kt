package com.stringconcat.ddd.order.usecase.rules

import com.stringconcat.ddd.order.domain.cart.CustomerId
import com.stringconcat.ddd.order.domain.rules.CustomerHasActiveOrderRule
import com.stringconcat.ddd.order.usecase.order.CustomerOrderExtractor

class CustomerHasActiveOrderRuleImpl(val customerOrderExtractor: CustomerOrderExtractor) : CustomerHasActiveOrderRule {
    override fun hasActiveOrder(customerId: CustomerId): Boolean {
        return customerOrderExtractor.getActiveOrderByCustomerId(customerId) != null
    }
}