package com.stringconcat.ddd.shop.persistence.postgresql

import javax.sql.DataSource
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

@SpringJUnitConfig(classes = [TestConfiguration::class])
internal class PgMealIdGeneratorTest {

    @Autowired
    private lateinit var dataSource: DataSource

    @Test
    fun `dummy test`() {
        val generator = PgMealIdGenerator()
        generator.generate()
        dataSource.connection
    }
}