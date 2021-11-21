package com.stringconcat.ddd.shop.persistence.postgresql

import arrow.core.getOrElse
import com.stringconcat.ddd.common.types.base.Version
import com.stringconcat.ddd.shop.domain.menu.Meal
import com.stringconcat.ddd.shop.domain.menu.MealDescription
import com.stringconcat.ddd.shop.domain.menu.MealId
import com.stringconcat.ddd.shop.domain.menu.MealName
import com.stringconcat.ddd.shop.domain.menu.MealRestorer
import com.stringconcat.ddd.shop.domain.menu.Price
import java.math.BigDecimal
import java.sql.ResultSet
import org.springframework.jdbc.core.ResultSetExtractor
import org.springframework.jdbc.core.RowMapper

class MealResultSetExtractor : ResultSetExtractor<Meal> {

    override fun extractData(rs: ResultSet): Meal? {
        return if (rs.next()) {
            MealRowMapper().mapRow(rs, 0)
        } else {
            null
        }
    }
}

class MealRowMapper : RowMapper<Meal> {

    override fun mapRow(rs: ResultSet, rowNum: Int): Meal {
        val id = rs.getLong("id")
        val name = rs.getString("name")
        val description = rs.getString("description")
        val price = rs.getBigDecimal("price")
        val removed = rs.getBoolean("removed")
        val version = rs.getLong("version")

        return restoreMeal(id = id,
            name = name,
            description = description,
            price = price,
            removed = removed,
            version = version)
    }

    private fun restoreMeal(
        id: Long,
        name: String,
        description: String,
        price: BigDecimal,
        removed: Boolean,
        version: Long,
    ): Meal {

        val mealName = MealName.from(name).getOrElse {
            error("Meal name $name is incorrect for Meal #$id")
        }

        val mealDescription = MealDescription.from(description)
            .getOrElse {
                error("Meal description $description is incorrect for Meal #$id")
            }

        val mealPrice = Price.from(price).getOrElse {
            error("Meal price $price is incorrect for Meal #$id")
        }

        return MealRestorer.restoreMeal(
            id = MealId(id),
            name = mealName,
            description = mealDescription,
            price = mealPrice,
            version = Version.from(version),
            removed = removed)
    }
}