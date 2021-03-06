package com.stringconcat.ddd.shop.domain.order

import com.stringconcat.ddd.common.types.base.Version
import com.stringconcat.ddd.common.types.common.Address
import com.stringconcat.ddd.shop.domain.cart.CustomerId
import java.time.OffsetDateTime

object ShopOrderRestorer {

    fun restoreOrder(
        id: ShopOrderId,
        created: OffsetDateTime,
        forCustomer: CustomerId,
        address: Address,
        orderItems: Set<OrderItem>,
        state: OrderState,
        version: Version
    ): ShopOrder {

        return ShopOrder(
            id = id,
            created = created,
            forCustomer = forCustomer,
            address = address,
            orderItems = orderItems,
            version = version
        ).apply {
            this.state = state
        }
    }
}