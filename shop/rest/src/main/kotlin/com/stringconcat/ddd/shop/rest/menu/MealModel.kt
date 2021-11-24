package com.stringconcat.ddd.shop.rest.menu

import com.stringconcat.ddd.shop.usecase.menu.dto.MealInfo
import java.math.BigDecimal
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.Relation
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn

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
                price = mealInfo.price.toBigDecimalValue(),
                description = mealInfo.description.value,
                version = mealInfo.version.value)

                .add(linkTo(methodOn(GetMealByIdEndpoint::class.java)
                    .execute(mealInfo.id.value))
                    .withSelfRel())

                .add(linkTo(methodOn(RemoveMealFromMenuEndpoint::class.java)
                    .execute(mealInfo.id.value))
                    .withRel("remove"))
    }
}