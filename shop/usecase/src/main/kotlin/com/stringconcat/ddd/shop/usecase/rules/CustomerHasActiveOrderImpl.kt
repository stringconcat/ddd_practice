package com.stringconcat.ddd.shop.usecase.rules

import com.stringconcat.ddd.shop.domain.cart.CustomerId
import com.stringconcat.ddd.shop.domain.order.CustomerHasActiveOrder
import com.stringconcat.ddd.shop.usecase.order.ShopOrderExtractor

class CustomerHasActiveOrderImpl(
    private val shopOrderExtractor: ShopOrderExtractor
) : CustomerHasActiveOrder {

    override fun check(forCustomer: CustomerId): Boolean {
        val lastOrder = shopOrderExtractor.getLastOrder(forCustomer)
        return lastOrder != null && lastOrder.isActive()
    }
}