package com.stringconcat.ddd.order.domain.payment

import com.stringconcat.ddd.common.types.base.ValueObject
import com.stringconcat.ddd.order.domain.menu.Price
import com.stringconcat.ddd.order.domain.order.CustomerOrderId

data class OrderPayment(
    val orderId: CustomerOrderId,
    val price: Price
) : ValueObject
