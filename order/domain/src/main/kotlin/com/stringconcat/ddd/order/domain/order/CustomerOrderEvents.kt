package com.stringconcat.ddd.order.domain.order

import com.stringconcat.ddd.common.types.base.DomainEvent

data class CustomerOrderCreatedDomainEvent(val orderId: CustomerOrderId) : DomainEvent()
data class CustomerOrderCompletedDomainEvent(val orderId: CustomerOrderId) : DomainEvent()
data class CustomerOrderConfirmedDomainEvent(val orderId: CustomerOrderId) : DomainEvent()
data class CustomerOrderCancelledDomainEvent(val orderId: CustomerOrderId) : DomainEvent()
data class CustomerOrderHasBeenDomainEvent(val orderId: CustomerOrderId) : DomainEvent()