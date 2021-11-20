package com.stringconcat.ddd.shop.persistence.postgresql

import com.stringconcat.ddd.shop.domain.mealId
import com.stringconcat.ddd.shop.domain.menu.MealId
import io.kotest.matchers.shouldBe
import javax.sql.DataSource
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

@SpringJUnitConfig(classes = [TestConfiguration::class])
internal class PostgresMealIdGeneratorTest {

    @Autowired
    private lateinit var dataSource: DataSource

    @Autowired
    private lateinit var jdbcTemplate: NamedParameterJdbcTemplate

    @Test
    fun `generate id`() {
        val id = mealId()
        jdbcTemplate.jdbcTemplate.execute("SELECT setval('shop.meal_id_seq', ${id.value});")
        val generator = PostgresMealIdGenerator(dataSource)

        val mealId = generator.generate()

        mealId shouldBe MealId(id.value + 1)
    }
}