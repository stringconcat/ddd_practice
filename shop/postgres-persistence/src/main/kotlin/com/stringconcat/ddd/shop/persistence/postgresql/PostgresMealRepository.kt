package com.stringconcat.ddd.shop.persistence.postgresql

import com.stringconcat.ddd.common.events.DomainEventPublisher
import com.stringconcat.ddd.shop.domain.menu.Meal
import com.stringconcat.ddd.shop.usecase.menu.access.MealPersister
import javax.sql.DataSource
import org.springframework.dao.DuplicateKeyException
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

class PostgresMealRepository(
    dataSource: DataSource,
    private val eventPublisher: DomainEventPublisher,
) : MealPersister {
    private val jdbcTemplate = NamedParameterJdbcTemplate(dataSource)

    @Suppress("SwallowedException") // временно, потом уберу
    override fun save(meal: Meal) {

        try {
            if (meal.version.isNew()) {
                insert(meal)
            } else {
                update(meal)
            }
        } catch (ex: DuplicateKeyException) {
            throw RaceConditionException(ex.message)
        }

        val events = meal.popEvents()
        eventPublisher.publish(events)
    }

    private fun update(meal: Meal) {
        val params = mapOf(
            "id" to meal.id.value,
            "name" to meal.name.value,
            "description" to meal.description.value,
            "price" to meal.price.value,
            "removed" to meal.removed,
            "previousVersion" to meal.version.previous().value,
            "currentVersion" to meal.version.value)

        val updated = jdbcTemplate.update("""
            UPDATE shop.meal SET name = :name,
            description = :description,
            price = :price,
            removed = :removed,
            version = :currentVersion
            WHERE id = :id AND version = :previousVersion          
        """.trimIndent(), params)

        if (updated == 0) {
            throw RaceConditionException("Meal #${meal.id.value} [version = ${meal.version.value}] is outdated")
        }
    }

    private fun insert(meal: Meal) {
        val params = mapOf(
            "id" to meal.id.value,
            "name" to meal.name.value,
            "description" to meal.description.value,
            "price" to meal.price.value,
            "removed" to meal.removed,
            "version" to meal.version.value)

        jdbcTemplate.update("""INSERT INTO shop.meal (id, name, description, price, removed, version)
            VALUES (:id, :name, :description, :price, :removed, :version)
        """.trimMargin(), params)
    }
}