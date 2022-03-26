package com.stringconcat.ddd.shop.rest.menu

import com.stringconcat.ddd.shop.usecase.menu.dto.MealInfo
import io.swagger.annotations.ApiModelProperty
import java.math.BigDecimal

data class MealModel(
    @ApiModelProperty(notes = "ID of the meal", name = "id") val id: Long,
    @ApiModelProperty(notes = "Name of the meal", name = "name") val name: String,
    @ApiModelProperty(notes = "Description of the meal", name = "description") val description: String,
    @ApiModelProperty(notes = "Price of the meal", name = "price") val price: BigDecimal,
    @ApiModelProperty(notes = "Version of the meal", name = "version") val version: Long,
) {

    companion object {
        fun from(mealInfo: MealInfo): MealModel =
                MealModel(id = mealInfo.id.toLongValue(),
                        name = mealInfo.name.toStringValue(),
                        price = mealInfo.price.toBigDecimalValue(),
                        description = mealInfo.description.toStringValue(),
                        version = mealInfo.version.toLongValue())
    }
}