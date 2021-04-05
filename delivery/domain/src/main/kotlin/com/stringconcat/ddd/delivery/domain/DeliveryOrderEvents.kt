package com.stringconcat.ddd.delivery.domain

import com.stringconcat.ddd.common.types.base.DomainEvent

data class DeliveryOrderCreatedDomainEvent(val orderId: DeliveryOrderId): DomainEvent()
