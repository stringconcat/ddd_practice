package com.stringconcat.ddd.order.domain.order

import com.stringconcat.ddd.common.types.base.Version
import com.stringconcat.ddd.order.domain.cart.CustomerId
import java.time.OffsetDateTime

object CustomerOrderRestorer {

    fun restoreOrder(
        id: OrderId,
        created: OffsetDateTime,
        customerId: CustomerId,
        orderItems: Set<OrderItem>,
        state: OrderState,
        version: Version
    ): CustomerOrder {

        return CustomerOrder(
            id = id,
            created = created,
            customerId = customerId,
            orderItems = orderItems,
            version = version
        ).apply {
            this.state = state
        }
    }
}