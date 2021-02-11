package com.stringconcat.ddd.order.domain.meal

import com.stringconcat.ddd.common.types.base.DomainEvent

data class MealCreated(val mealId: MealId) : DomainEvent()
data class MealRemoved(val mealId: MealId) : DomainEvent()