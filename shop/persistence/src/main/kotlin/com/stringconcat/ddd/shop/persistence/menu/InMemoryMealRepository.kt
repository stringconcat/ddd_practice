package com.stringconcat.ddd.shop.persistence.menu

import com.stringconcat.ddd.common.events.DomainEventPublisher
import com.stringconcat.ddd.shop.domain.menu.Meal
import com.stringconcat.ddd.shop.domain.menu.MealId
import com.stringconcat.ddd.shop.domain.menu.MealName
import com.stringconcat.ddd.shop.usecase.menu.access.MealExtractor
import com.stringconcat.ddd.shop.usecase.menu.access.MealPersister

class InMemoryMealRepository(private val eventPublisher: DomainEventPublisher) : MealExtractor, MealPersister {

    internal val storage = LinkedHashMap<MealId, Meal>()

    override fun getById(id: MealId) = storage[id]

    override fun getByName(name: MealName) =
        storage.values.firstOrNull { it.name == name }

    override fun getAll() = storage.values.filterNot { it.removed }.toList()

    override fun save(meal: Meal) {
        eventPublisher.publish(meal.popEvents())
        storage[meal.id] = meal
    }
}