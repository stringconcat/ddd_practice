package com.stringconcat.ddd.order.domain.cart

import com.stringconcat.ddd.common.types.base.DomainEvent
import com.stringconcat.ddd.order.domain.menu.MealId

data class CartHasBeenCreatedEvent(val cartId: CartId) : DomainEvent()

data class MealHasBeenAddedToCart(val cartId: CartId, val mealId: MealId) : DomainEvent()
data class MealHasBeenRemovedFromCart(val cartId: CartId, val mealId: MealId) : DomainEvent()