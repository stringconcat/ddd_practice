package com.stringconcat.ddd.shop.usecase.order

import com.stringconcat.ddd.shop.domain.cart.CustomerId
import com.stringconcat.ddd.shop.domain.order.CustomerOrder
import com.stringconcat.ddd.shop.domain.order.CustomerOrderId

interface CustomerOrderExtractor {
    fun getById(orderId: CustomerOrderId): CustomerOrder?

    fun getLastOrder(forCustomer: CustomerId): CustomerOrder?

    fun getAll(): List<CustomerOrder>
}