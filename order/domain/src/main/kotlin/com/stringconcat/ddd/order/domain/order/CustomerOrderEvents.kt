package com.stringconcat.ddd.order.domain.order

import com.stringconcat.ddd.common.types.base.DomainEvent

data class CustomerOrderHasBeenCreatedEvent(val orderId: CustomerOrderId) : DomainEvent()
data class CustomerOrderHasBeenCompletedEvent(val orderId: CustomerOrderId) : DomainEvent()
data class CustomerOrderHasBeenConfirmedEvent(val orderId: CustomerOrderId) : DomainEvent()
data class CustomerOrderHasBeenCancelledEvent(val orderId: CustomerOrderId) : DomainEvent()
data class CustomerOrderHasBeenPaidEvent(val orderId: CustomerOrderId) : DomainEvent()