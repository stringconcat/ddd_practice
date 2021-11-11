package com.stringconcat.ddd.shop.rest.menu

import com.stringconcat.ddd.shop.domain.menu.MealId
import com.stringconcat.ddd.shop.rest.API_V1_MENU
import com.stringconcat.ddd.shop.rest.menu.views.MealModel
import com.stringconcat.ddd.shop.usecase.menu.GetMealById
import com.stringconcat.ddd.shop.usecase.menu.GetMealByIdUseCaseError
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class GetMealByIdEndpoint(private val getMealById: GetMealById) {

    @GetMapping(path = ["$API_V1_MENU/{id}"])
    fun execute(@PathVariable("id") mealId: Long): ResponseEntity<*> {
        return getMealById.execute(MealId(mealId))
            .fold({
                when (it) {
                    is GetMealByIdUseCaseError.MealNotFound -> resourceNotFound()
                }
            }, { mealInfo ->
                ResponseEntity.ok(MealModel(id = mealInfo.id.value,
                    name = mealInfo.name.value,
                    price = mealInfo.price.value,
                    description = mealInfo.description.value,
                    version = mealInfo.version.value))
            })
    }
}