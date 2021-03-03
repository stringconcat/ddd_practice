package com.stringconcat.ddd.kitchen.domain.order

import com.stringconcat.ddd.common.types.base.DomainEvent

data class KitchenOrderCreatedDomainEvent(val orderId: KitchenOrderId) : DomainEvent()
data class KitchenOrderCookedDomainEvent(val orderId: KitchenOrderId) : DomainEvent()