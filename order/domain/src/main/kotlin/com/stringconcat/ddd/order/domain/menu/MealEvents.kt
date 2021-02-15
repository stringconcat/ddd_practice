package com.stringconcat.ddd.order.domain.menu

import com.stringconcat.ddd.common.types.base.DomainEvent

data class MealHasBeenAddedToMenu(val mealId: MealId) : DomainEvent()
data class MealHasBeenRemovedFromMenu(val mealId: MealId) : DomainEvent()