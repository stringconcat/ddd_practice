package com.stringconcat.ddd.shop.rest.menu.views

import com.stringconcat.ddd.shop.usecase.menu.dto.MealInfo
import java.math.BigDecimal
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.Relation

@Relation(collectionRelation = "meals")
data class MealModel(
    val id: Long,
    val name: String,
    val description: String,
    val price: BigDecimal,
    val version: Long,
) : RepresentationModel<MealModel>() {

    companion object {
        fun from(mealInfo: MealInfo): MealModel =
            MealModel(id = mealInfo.id.value,
                name = mealInfo.name.value,
                price = mealInfo.price.value,
                description = mealInfo.description.value,
                version = mealInfo.version.value)
    }
}