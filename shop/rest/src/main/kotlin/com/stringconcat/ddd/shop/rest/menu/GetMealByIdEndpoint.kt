package com.stringconcat.ddd.shop.rest.menu

import com.stringconcat.ddd.common.rest.resourceNotFound
import com.stringconcat.ddd.shop.domain.menu.MealId
import com.stringconcat.ddd.shop.rest.API_V1_MENU_GET_BY_ID
import com.stringconcat.ddd.shop.usecase.menu.GetMealById
import com.stringconcat.ddd.shop.usecase.menu.GetMealByIdUseCaseError
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@Api(tags = ["Menu"])
@RestController
class GetMealByIdEndpoint(private val getMealById: GetMealById) {

    @ApiOperation("Get a meal by id")
    @GetMapping(path = [API_V1_MENU_GET_BY_ID])
    fun execute(@PathVariable("id") mealId: Long): ResponseEntity<*> =
        getMealById.execute(MealId(mealId))
            .fold({
                it.toRestError()
            }, { mealInfo ->
                ResponseEntity.ok(MealModel.from(mealInfo))
            })
}

fun GetMealByIdUseCaseError.toRestError() =
    when (this) {
        is GetMealByIdUseCaseError.MealNotFound -> resourceNotFound()
    }