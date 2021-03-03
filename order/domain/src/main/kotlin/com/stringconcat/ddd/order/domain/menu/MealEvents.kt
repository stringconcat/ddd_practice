package com.stringconcat.ddd.order.domain.menu

import com.stringconcat.ddd.common.types.base.DomainEvent

data class MealAddedToMenuDomainEvent(val mealId: MealId) : DomainEvent()
data class MealRemovedFromMenuDomainEvent(val mealId: MealId) : DomainEvent()