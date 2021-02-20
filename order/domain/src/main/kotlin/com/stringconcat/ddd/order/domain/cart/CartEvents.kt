package com.stringconcat.ddd.order.domain.cart

import com.stringconcat.ddd.common.types.base.DomainEvent
import com.stringconcat.ddd.order.domain.menu.MealId

data class CartCreatedDomainEvent(val cartId: CartId) : DomainEvent()
data class MealAddedToCartDomainEvent(val cartId: CartId, val mealId: MealId) : DomainEvent()
data class MealRemovedFromCartDomainEvent(val cartId: CartId, val mealId: MealId) : DomainEvent()