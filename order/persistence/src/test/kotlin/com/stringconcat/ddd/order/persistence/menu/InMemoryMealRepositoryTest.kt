package com.stringconcat.ddd.order.persistence.menu

import com.stringconcat.ddd.order.domain.menu.MealRemovedFromMenuDomainEvent
import com.stringconcat.ddd.order.persistence.TestEventPublisher
import com.stringconcat.ddd.order.persistence.meal
import com.stringconcat.ddd.order.persistence.mealId
import com.stringconcat.ddd.order.persistence.mealName
import com.stringconcat.ddd.order.persistence.mealWithEvents
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.matchers.types.shouldBeSameInstanceAs
import org.junit.jupiter.api.Test

internal class InMemoryMealRepositoryTest {
    private val eventPublisher = TestEventPublisher()

    @Test
    fun `saving meal - meal doesn't exist`() {
        val repository = InMemoryMealRepository(eventPublisher)
        val meal = mealWithEvents()

        repository.save(meal)

        val storedMeal = repository.storage[meal.id]
        storedMeal shouldBeSameInstanceAs meal
        eventPublisher.storage.shouldHaveSize(1)

        val event = eventPublisher.storage.first()
        event.shouldBeInstanceOf<MealRemovedFromMenuDomainEvent>()
        event.mealId shouldBe meal.id
    }

    @Test
    fun `saving meal - meal exists`() {

        val id = mealId()
        val existingMeal = meal(id = id)

        val repository = InMemoryMealRepository(eventPublisher)
        repository.storage[existingMeal.id] = existingMeal

        val updatedMeal = mealWithEvents(id)
        repository.save(updatedMeal)

        val event = eventPublisher.storage.first()
        event.shouldBeInstanceOf<MealRemovedFromMenuDomainEvent>()
        event.mealId shouldBe updatedMeal.id
    }

    @Test
    fun `get by id - meal exists`() {
        val existingMeal = meal()

        val repository = InMemoryMealRepository(eventPublisher)
        repository.storage[existingMeal.id] = existingMeal

        val meal = repository.getById(existingMeal.id)
        meal shouldBeSameInstanceAs existingMeal
    }

    @Test
    fun `get by id - meal doesn't exist`() {
        val repository = InMemoryMealRepository(eventPublisher)
        val meal = repository.getById(mealId())
        meal.shouldBeNull()
    }

    @Test
    fun `get meal by name - repository is empty`() {
        val repository = InMemoryMealRepository(eventPublisher)
        val meal = repository.getByName(mealName())
        meal.shouldBeNull()
    }

    @Test
    fun `get meal by name - success`() {

        val storedMeal = meal()

        val repository = InMemoryMealRepository(eventPublisher)
        repository.save(storedMeal)

        val meal = repository.getByName(storedMeal.name)
        meal shouldBeSameInstanceAs storedMeal
    }

    @Test
    fun `get all meals - repository is empty`() {
        val repository = InMemoryMealRepository(eventPublisher)
        val meals = repository.getAll()
        meals.shouldBeEmpty()
    }

    @Test
    fun `get all meals - success`() {
        val repository = InMemoryMealRepository(eventPublisher)
        val storedMeal = meal()
        repository.storage[storedMeal.id] = storedMeal

        val meals = repository.getAll()
        meals shouldContainExactly listOf(storedMeal)
    }

    @Test
    fun `get all meals - removed is not returned`() {
        val repository = InMemoryMealRepository(eventPublisher)
        val storedMeal = meal(removed = true)
        repository.storage[storedMeal.id] = storedMeal

        val meals = repository.getAll()
        meals.shouldBeEmpty()
    }
}