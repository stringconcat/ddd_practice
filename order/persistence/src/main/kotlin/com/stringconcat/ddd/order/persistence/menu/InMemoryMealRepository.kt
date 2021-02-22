package com.stringconcat.ddd.order.persistence.menu

import com.stringconcat.ddd.common.types.base.EventPublisher
import com.stringconcat.ddd.order.domain.menu.Meal
import com.stringconcat.ddd.order.domain.menu.MealId
import com.stringconcat.ddd.order.domain.menu.MealName
import com.stringconcat.ddd.order.usecase.menu.MealExtractor
import com.stringconcat.ddd.order.usecase.menu.MealPersister

class InMemoryMealRepository(private val eventPublisher: EventPublisher) : MealExtractor, MealPersister {

    internal val storage = LinkedHashMap<MealId, Meal>()

    override fun getById(id: MealId) = storage[id]

    override fun getByName(name: MealName) =
        storage.values.firstOrNull { it.name == name }

    override fun save(meal: Meal) {
        eventPublisher.publish(meal.popEvents())
        storage[meal.id] = meal
    }
}