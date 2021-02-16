package com.stringconcat.ddd.kitchen.domain.order

import com.stringconcat.ddd.common.types.base.DomainEvent

data class KitchenOrderHasBeenCreatedEvent(val orderId: KitchenOrderId) : DomainEvent()
data class KitchenOrderHasBeenCookedEvent(val orderId: KitchenOrderId) : DomainEvent()