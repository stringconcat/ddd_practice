package com.stringconcat.ddd.shop.domain.order

import com.stringconcat.ddd.shop.domain.cart.CustomerId

fun interface CustomerHasActiveOrder {
    operator fun invoke(forCustomer: CustomerId): Boolean
}