package com.stringconcat.ddd.order.domain.order

import com.stringconcat.ddd.common.types.base.DomainEvent
import com.stringconcat.ddd.order.domain.cart.CustomerId

data class CustomerOrderCreatedDomainEvent(val orderId: CustomerOrderId, val forCustomer: CustomerId) : DomainEvent()
data class CustomerOrderCompletedDomainEvent(val orderId: CustomerOrderId) : DomainEvent()
data class CustomerOrderConfirmedDomainEvent(val orderId: CustomerOrderId) : DomainEvent()
data class CustomerOrderCancelledDomainEvent(val orderId: CustomerOrderId) : DomainEvent()
data class CustomerOrderHasBeenPaidDomainEvent(val orderId: CustomerOrderId) : DomainEvent()