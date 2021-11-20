package com.stringconcat.ddd.shop.persistence.postgresql

import arrow.core.Either
import arrow.core.leftIor
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
import com.stringconcat.ddd.shop.domain.menu.MealRestorer
import com.stringconcat.ddd.shop.domain.price
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

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

fun Meal.copy() =
    MealRestorer.restoreMeal(
        id = this.id,
        name = this.name,
        description = this.description,
        version = this.version,
        removed = this.removed,
        price = this.price
    )

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

fun Meal.checkExistsInDatabase(jdbcTemplate: NamedParameterJdbcTemplate) {

    val params = mapOf(
        "id" to this.id.value,
        "name" to this.name.value,
        "description" to this.description.value,
        "removed" to this.removed,
        "price" to this.price.value,
        "version" to this.version.value)

    val exists = jdbcTemplate.queryForObject("""
            SELECT EXISTS(SELECT 1 FROM shop.meal 
                WHERE id=:id 
                AND name = :name 
                AND description = :description
                AND price = :price
                AND removed = :removed
                AND version = :version)
            
        """.trimIndent(), params, Boolean::class.java)

    exists shouldBe true
}