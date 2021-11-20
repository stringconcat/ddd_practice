package com.stringconcat.ddd.shop.persistence.postgresql

import arrow.core.Either
import com.stringconcat.ddd.common.events.DomainEventPublisher
import com.stringconcat.ddd.common.types.base.DomainEvent
import com.stringconcat.ddd.shop.domain.mealDescription
import com.stringconcat.ddd.shop.domain.mealId
import com.stringconcat.ddd.shop.domain.mealName
import com.stringconcat.ddd.shop.domain.menu.Meal
import com.stringconcat.ddd.shop.domain.menu.MealAlreadyExists
import com.stringconcat.ddd.shop.domain.menu.MealId
import com.stringconcat.ddd.shop.domain.menu.MealIdGenerator
import com.stringconcat.ddd.shop.domain.menu.MealName
import com.stringconcat.ddd.shop.domain.price
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder

fun newMeal(mealId: MealId = mealId(), mealName: MealName = mealName()): Meal {

    val name = mealName
    val description = mealDescription()
    val price = price()

    val generator = object : MealIdGenerator {
        override fun generate() = mealId
    }

    val rule = object : MealAlreadyExists {
        override fun check(name: MealName) = false
    }

    val result = Meal.addMealToMenu(idGenerator = generator,
        mealExists = rule,
        name = name,
        description = description,
        price = price)

    check(result is Either.Right) { "Meal should be right" }
    return result.value
}

class MockEventPublisher : DomainEventPublisher {
    private val events = ArrayList<DomainEvent>()

    override fun publish(events: Collection<DomainEvent>) {
        this.events.addAll(events)
    }

    fun verifyContains(events: Collection<DomainEvent>) {
        this.events shouldContainExactlyInAnyOrder events
    }

    fun verifyEventsIsEmpty() {
        this.events.shouldBeEmpty()
    }
}