package com.stringconcat.ddd.shop.persistence.postgresql

import com.stringconcat.ddd.common.events.DomainEventPublisher
import com.stringconcat.ddd.shop.domain.menu.Meal
import com.stringconcat.ddd.shop.domain.menu.MealAddedToMenuDomainEvent
import com.stringconcat.ddd.shop.domain.menu.MealId
import com.stringconcat.ddd.shop.domain.menu.MealName
import com.stringconcat.ddd.shop.usecase.menu.access.MealExtractor
import com.stringconcat.ddd.shop.usecase.menu.access.MealPersister
import javax.sql.DataSource
import org.springframework.dao.DuplicateKeyException
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

class PostgresMealRepository(
    dataSource: DataSource,
    private val eventPublisher: DomainEventPublisher,
) : MealPersister, MealExtractor {
    private val jdbcTemplate = NamedParameterJdbcTemplate(dataSource)

    override fun save(meal: Meal) {
        val events = meal.popEvents()
        if (events.isNotEmpty()) {
            try {
                if (events.contains(MealAddedToMenuDomainEvent(meal.id))) {
                    insert(meal)
                } else {
                    update(meal)
                }
            } catch (ex: DuplicateKeyException) {
                throw StorageConflictException(cause = ex)
            }
            eventPublisher.publish(events)
        }
    }

    private fun update(meal: Meal) {
        val params = mapOf(
            "id" to meal.id.value,
            "name" to meal.name.value,
            "description" to meal.description.value,
            "price" to meal.price.toBigDecimalValue(),
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
            throw StorageConflictException("Meal #${meal.id.value} [version = ${meal.version.value}] is outdated")
        }
    }

    private fun insert(meal: Meal) {
        val params = mapOf(
            "id" to meal.id.value,
            "name" to meal.name.value,
            "description" to meal.description.value,
            "price" to meal.price.toBigDecimalValue(),
            "removed" to meal.removed,
            "version" to meal.version.value)

        jdbcTemplate.update("""INSERT INTO shop.meal (id, name, description, price, removed, version)
            VALUES (:id, :name, :description, :price, :removed, :version)
        """.trimMargin(), params)
    }

    override fun getById(id: MealId): Meal? {
        val params = mapOf("id" to id.value)
        return jdbcTemplate.query("SELECT * FROM shop.meal WHERE id = :id", params,
            MealResultSetExtractor())
    }

    override fun getByName(name: MealName): Meal? {
        val params = mapOf("name" to name.value)
        return jdbcTemplate.query("SELECT * FROM shop.meal WHERE name = :name",
            params, MealResultSetExtractor())
    }

    override fun getAll(): List<Meal> {
        return jdbcTemplate.query("SELECT * FROM shop.meal", MealRowMapper())
    }
}