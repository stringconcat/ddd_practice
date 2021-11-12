package com.stringconcat.ddd.shop.rest.menu

import arrow.core.zip
import com.stringconcat.ddd.shop.domain.menu.MealDescription
import com.stringconcat.ddd.shop.domain.menu.MealName
import com.stringconcat.ddd.shop.domain.menu.Price
import com.stringconcat.ddd.shop.rest.API_V1_MENU
import com.stringconcat.ddd.shop.usecase.menu.AddMealToMenu
import com.stringconcat.ddd.shop.usecase.menu.AddMealToMenuUseCaseError
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal

@RestController
class AddMealToMenuEndpoint(val addMealToMenu: AddMealToMenu) {

    @PostMapping(path = [API_V1_MENU])
    fun execute(@RequestBody request: AddMealToMenuRestRequest): ResponseEntity<*> {
        return MealName.validated(request.name)
            .zip(
                MealDescription.validated(request.description),
                Price.validated(request.price)
            ) { mealName: MealName, mealDescription: MealDescription, price: Price ->
                addMealToMenu.execute(mealName, mealDescription, price)
            }.fold({ validationErrors ->
                validationErrors.toInvalidParamsBadRequest()
            }, { addingMealToMenuResult ->
                addingMealToMenuResult.fold(
                    { it.toRestError() },
                    { created(linkTo(methodOn(GetMenuEndpoint::class.java).execute()).toUri()) })
            })
    }
}

fun AddMealToMenuUseCaseError.toRestError() =
    when (this) {
        is AddMealToMenuUseCaseError.AlreadyExists ->
            restBusinessError(
                RestBusinessError(title = "Meal already exists", code = "already_exists")
            )
    }

data class AddMealToMenuRestRequest(
    val name: String,
    val description: String,
    val price: BigDecimal,
)