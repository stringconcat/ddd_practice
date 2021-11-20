package com.stringconcat.ddd.shop.persistence.postgresql

import com.stringconcat.ddd.shop.domain.mealName
import com.stringconcat.ddd.shop.domain.menu.Meal
import com.stringconcat.ddd.shop.domain.menu.MealAddedToMenuDomainEvent
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import javax.sql.DataSource
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

@SpringJUnitConfig(classes = [TestConfiguration::class])
class PostgresMealRepositoryTest {

    @Autowired
    private lateinit var dataSource: DataSource

    @Autowired
    private lateinit var jdbcTemplate: NamedParameterJdbcTemplate

    @Test
    fun `save new instance`() {

        val publisher = MockEventPublisher()
        val postgresMealRepository = PostgresMealRepository(dataSource, publisher)
        val meal = newMeal()

        postgresMealRepository.save(meal)

        checkExistsInDatabase(meal)
        publisher.verifyContains(listOf(MealAddedToMenuDomainEvent(mealId = meal.id)))
    }

    @Test
    fun `save new instance but already exists with the same id`() {
        val publisher = MockEventPublisher()
        val postgresMealRepository = PostgresMealRepository(dataSource, publisher)

        val meal = newMeal()
        postgresMealRepository.save(meal)

        shouldThrow<RaceConditionException> {
            postgresMealRepository.save(meal)
        }
    }

    @Test
    fun `save new instance but already exists with the same name`() {
        val publisher = MockEventPublisher()
        val postgresMealRepository = PostgresMealRepository(dataSource, publisher)

        val mealName = mealName()
        val first = newMeal(mealName = mealName)
        postgresMealRepository.save(first)

        val second = newMeal(mealName = mealName)

        shouldThrow<RaceConditionException> {
            postgresMealRepository.save(second)
        }
    }

    @Test
    fun `create new instance and then update it`() {
        val postgresMealRepository = PostgresMealRepository(dataSource, MockEventPublisher())
        val meal = newMeal()
        postgresMealRepository.save(meal)

        meal.removeMealFromMenu()
        postgresMealRepository.save(meal)

        checkExistsInDatabase(meal)
    }

    // saving failed if version is outdated
    private fun checkExistsInDatabase(meal: Meal) {

        val params = mapOf(
            "id" to meal.id.value,
            "name" to meal.name.value,
            "description" to meal.description.value,
            "removed" to meal.removed,
            "price" to meal.price.value,
            "version" to meal.version.value)

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
}