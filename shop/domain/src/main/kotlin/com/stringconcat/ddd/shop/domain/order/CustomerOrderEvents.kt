package com.stringconcat.ddd.shop.domain.order

import com.stringconcat.ddd.common.types.base.DomainEvent
import com.stringconcat.ddd.shop.domain.cart.CustomerId
import com.stringconcat.ddd.shop.domain.menu.Price

data class ShopOrderCreatedDomainEvent(
    val orderId: ShopOrderId,
    val forCustomer: CustomerId,
    val totalPrice: Price,
) : DomainEvent()

data class ShopOrderCompletedDomainEvent(val orderId: ShopOrderId) : DomainEvent()
data class ShopOrderConfirmedDomainEvent(val orderId: ShopOrderId) : DomainEvent()
data class ShopOrderCancelledDomainEvent(val orderId: ShopOrderId) : DomainEvent()
data class ShopOrderPaidDomainEvent(val orderId: ShopOrderId) : DomainEvent()