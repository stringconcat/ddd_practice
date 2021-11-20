package com.stringconcat.ddd.shop.persistence.postgresql

import com.stringconcat.ddd.shop.domain.mealId
import com.stringconcat.ddd.shop.domain.mealName
import com.stringconcat.ddd.shop.domain.menu.MealAddedToMenuDomainEvent
import io.kotest.assertions.throwables.shouldThrow
import javax.sql.DataSource
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

@SpringJUnitConfig(classes = [TestConfiguration::class])
class PostgresMealRepositoryWriteTest {

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

        meal.checkExistsInDatabase(jdbcTemplate)
        publisher.verifyContains(listOf(MealAddedToMenuDomainEvent(mealId = meal.id)))
    }

    @Test
    fun `save new instance but already exists with the same id`() {
        val publisher = MockEventPublisher()
        val postgresMealRepository = PostgresMealRepository(dataSource, publisher)

        val mealId = mealId()
        val first = newMeal(mealId = mealId)
        val second = newMeal(mealId = mealId)
        postgresMealRepository.save(first)

        shouldThrow<StorageConflictException> {
            postgresMealRepository.save(second)
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

        shouldThrow<StorageConflictException> {
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

        meal.checkExistsInDatabase(jdbcTemplate)
    }

    @Test
    fun `save again without changes`() {
        val publisher = MockEventPublisher()
        val postgresMealRepository = PostgresMealRepository(dataSource, publisher)
        val meal = newMeal()
        postgresMealRepository.save(meal)

        postgresMealRepository.save(meal)

        meal.checkExistsInDatabase(jdbcTemplate)
        publisher.verifyContains(listOf(MealAddedToMenuDomainEvent(mealId = meal.id)))
    }

    @Test
    fun `saving failed if version is outdated`() {
        val postgresMealRepository = PostgresMealRepository(dataSource, MockEventPublisher())
        val meal = newMeal()
        postgresMealRepository.save(meal)

        val copyOfMeal = meal.copy()
        meal.removeMealFromMenu()
        postgresMealRepository.save(meal)
        copyOfMeal.removeMealFromMenu()

        shouldThrow<StorageConflictException> {
            postgresMealRepository.save(copyOfMeal)
        }
    }
}