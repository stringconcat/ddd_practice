package com.stringconcat.ddd.shop.rest.menu

import com.stringconcat.ddd.shop.usecase.menu.dto.MealInfo
import io.swagger.annotations.ApiModelProperty
import java.math.BigDecimal

data class MealModel(
    @ApiModelProperty("ID of the meal") val id: Long,
    @ApiModelProperty("Name of the meal") val name: String,
    @ApiModelProperty("Description of the meal") val description: String,
    @ApiModelProperty("Price of the meal") val price: BigDecimal,
    @ApiModelProperty("Version of the meal") val version: Long,
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