package com.stringconcat.ddd.shop.rest.menu

import arrow.core.zip
import com.stringconcat.ddd.shop.domain.menu.MealDescription
import com.stringconcat.ddd.shop.domain.menu.MealName
import com.stringconcat.ddd.shop.domain.menu.Price
import com.stringconcat.ddd.shop.usecase.menu.AddMealToMenu
import com.stringconcat.ddd.shop.usecase.menu.AddMealToMenuUseCaseError
import java.math.BigDecimal
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AddMealToMenuEndpoint(val addMealToMenu: AddMealToMenu) {

    @PostMapping("/rest/v1/menu")
    fun execute(@RequestBody request: AddMealToMenuRestRequest): ResponseEntity<*> {
        return MealName.validated(request.name)
            .zip(
                MealDescription.validated(request.description),
                Price.validated(request.price)
            ) { mealName: MealName, mealDescription: MealDescription, price: Price ->
                addMealToMenu.execute(mealName, mealDescription, price)
            }.fold({ // InvalidRequest
                validationError(it)
            }, { either ->
                either.fold(
                    { ResponseEntity.unprocessableEntity().body(it.toRestError()) },
                    { ResponseEntity.ok().body("TODO") })
            })
    }
}

fun AddMealToMenuUseCaseError.toRestError(): String =
    when (this) {
        is AddMealToMenuUseCaseError.AlreadyExists -> "already_exists"
    }

data class AddMealToMenuRestRequest(
    val name: String,
    val description: String,
    val price: BigDecimal,
)