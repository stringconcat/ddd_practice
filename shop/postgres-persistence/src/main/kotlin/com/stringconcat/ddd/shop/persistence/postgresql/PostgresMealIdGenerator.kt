package com.stringconcat.ddd.shop.persistence.postgresql

import com.stringconcat.ddd.shop.domain.menu.MealId
import com.stringconcat.ddd.shop.domain.menu.MealIdGenerator
import javax.sql.DataSource
import org.springframework.jdbc.core.JdbcTemplate

class PostgresMealIdGenerator(dataSource: DataSource) : MealIdGenerator {

    private val template = JdbcTemplate(dataSource)

    override fun generate(): MealId {
        val id = template.queryForObject("SELECT nextval('shop.meal_id_seq');", Long::class.java)!!
        return MealId(id)
    }
}