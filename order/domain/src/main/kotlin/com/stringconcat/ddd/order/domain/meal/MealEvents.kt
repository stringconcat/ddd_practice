package com.stringconcat.ddd.order.domain.meal

import com.stringconcat.ddd.common.types.base.DomainEvent

data class MealAddedToMenu(val mealId: MealId) : DomainEvent()
data class MealRemovedFromMenu(val mealId: MealId) : DomainEvent()