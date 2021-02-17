package com.stringconcat.ddd.order.domain.order

import com.stringconcat.ddd.common.types.base.Version
import com.stringconcat.ddd.common.types.common.Address
import com.stringconcat.ddd.order.domain.cart.CustomerId
import java.time.OffsetDateTime

object CustomerOrderRestorer {

    fun restoreOrder(
        id: CustomerOrderId,
        created: OffsetDateTime,
        customerId: CustomerId,
        address: Address,
        orderItems: Set<OrderItem>,
        state: OrderState,
        version: Version
    ): CustomerOrder {

        return CustomerOrder(
            id = id,
            created = created,
            customerId = customerId,
            address = address,
            orderItems = orderItems,
            version = version
        ).apply {
            this.state = state
        }
    }
}