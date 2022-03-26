package com.stringconcat.ddd.shop.rest.menu

import arrow.core.zip
import com.stringconcat.ddd.common.rest.created
import com.stringconcat.ddd.common.rest.restBusinessError
import com.stringconcat.ddd.common.rest.toInvalidParamsBadRequest
import com.stringconcat.ddd.shop.domain.menu.MealDescription
import com.stringconcat.ddd.shop.domain.menu.MealName
import com.stringconcat.ddd.shop.domain.menu.Price
import com.stringconcat.ddd.shop.rest.API_V1_MENU_ADD_TO_MENU
import com.stringconcat.ddd.shop.rest.API_V1_MENU_GET_BY_ID
import com.stringconcat.ddd.shop.usecase.menu.AddMealToMenu
import com.stringconcat.ddd.shop.usecase.menu.AddMealToMenuUseCaseError
import io.swagger.annotations.ApiModelProperty
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import org.springframework.web.util.UriComponentsBuilder
import java.math.BigDecimal

@RestController
class AddMealToMenuEndpoint(val addMealToMenu: AddMealToMenu) {

    @PostMapping(path = [API_V1_MENU_ADD_TO_MENU])
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
                    { created(UriComponentsBuilder
                            .fromHttpUrl(ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString()
                                    .plus(API_V1_MENU_GET_BY_ID))
                            .buildAndExpand(it.toLongValue()).toUri()) })
            })
    }
}

fun AddMealToMenuUseCaseError.toRestError() =
    when (this) {
        is AddMealToMenuUseCaseError.AlreadyExists ->
            restBusinessError(title = "Meal already exists", code = "already_exists")
    }

data class AddMealToMenuRestRequest(
    @ApiModelProperty(notes = "Name of the meal", name = "name", required = true) val name: String,
    @ApiModelProperty(notes = "Description of the meal", name = "description", required = true) val description: String,
    @ApiModelProperty(notes = "Price of the meal", name = "price", required = true) val price: BigDecimal,
)