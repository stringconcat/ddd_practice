package com.stringconcat.ddd.shop.domain.order

import com.stringconcat.ddd.common.types.base.DomainEvent
import com.stringconcat.ddd.shop.domain.cart.CustomerId

data class CustomerOrderCreatedDomainEvent(val orderId: CustomerOrderId, val forCustomer: CustomerId) : DomainEvent()
data class CustomerOrderCompletedDomainEvent(val orderId: CustomerOrderId) : DomainEvent()
data class CustomerOrderConfirmedDomainEvent(val orderId: CustomerOrderId) : DomainEvent()
data class CustomerOrderCancelledDomainEvent(val orderId: CustomerOrderId) : DomainEvent()
data class CustomerOrderHasBeenDomainEvent(val orderId: CustomerOrderId) : DomainEvent()