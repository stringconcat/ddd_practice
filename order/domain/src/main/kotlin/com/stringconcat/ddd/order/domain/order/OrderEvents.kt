package com.stringconcat.ddd.order.domain.order

import com.stringconcat.ddd.common.types.base.DomainEvent

data class OrderHasBeenCreatedEvent(val orderId: OrderId) : DomainEvent()
data class OrderHasBeenCompletedEvent(val orderId: OrderId) : DomainEvent()
data class OrderHasBeenConfirmedEvent(val orderId: OrderId) : DomainEvent()
data class OrderHasBeenCancelledEvent(val orderId: OrderId) : DomainEvent()
data class OrderHasBeenPaidEvent(val orderId: OrderId) : DomainEvent()