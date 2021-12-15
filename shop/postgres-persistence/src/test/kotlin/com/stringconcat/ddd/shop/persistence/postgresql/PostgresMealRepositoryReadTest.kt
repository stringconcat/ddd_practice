package com.stringconcat.ddd.shop.persistence.postgresql

import com.stringconcat.ddd.shop.domain.mealId
import com.stringconcat.ddd.shop.domain.mealName
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import javax.sql.DataSource
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

@SpringJUnitConfig(classes = [TestConfiguration::class])
class PostgresMealRepositoryReadTest {

    @Autowired
    private lateinit var dataSource: DataSource

    @Autowired
    private lateinit var jdbcTemplate: NamedParameterJdbcTemplate

    @BeforeEach
    fun before() {
        cleanMealTable(jdbcTemplate)
    }

    @Test
    fun `get by id - not found`() {
        val repository = PostgresMealRepository(dataSource, MockEventPublisher())

        val result = repository.getById(mealId())

        result.shouldBeNull()
    }

    @Test
    fun `get by id - successfully returned`() {
        val meal = newMeal()
        val repository = PostgresMealRepository(dataSource, MockEventPublisher())
        repository.save(meal)

        val result = repository.getById(meal.id)
        result.shouldNotBeNull()
        result.checkEquals(meal)
    }

    @Test
    fun `get by name - not found`() {
        val repository = PostgresMealRepository(dataSource, MockEventPublisher())

        val result = repository.getByName(mealName())

        result.shouldBeNull()
    }

    @Test
    fun `get by name - successfully returned`() {
        val meal = newMeal()
        val repository = PostgresMealRepository(dataSource, MockEventPublisher())
        repository.save(meal)

        val result = repository.getByName(meal.name)
        result.shouldNotBeNull()
        result.checkEquals(meal)
    }

    @Test
    fun `get all - table is empty`() {
        cleanMealTable(jdbcTemplate)

        val repository = PostgresMealRepository(dataSource, MockEventPublisher())

        val result = repository.getAll()

        result.shouldBeEmpty()
    }

    @Test
    fun `get all - table is not empty`() {
        val meal = newMeal()
        val repository = PostgresMealRepository(dataSource, MockEventPublisher())
        repository.save(meal)

        val result = repository.getAll()

        result.shouldHaveSize(1)
        result[0].checkEquals(meal)
    }

    @Test
    fun `get all - table is not empty but only removed`() {
        val meal = newMeal()
        meal.removeMealFromMenu()
        val repository = PostgresMealRepository(dataSource, MockEventPublisher())
        repository.save(meal)

        val result = repository.getAll()
        result.shouldBeEmpty()
    }
}