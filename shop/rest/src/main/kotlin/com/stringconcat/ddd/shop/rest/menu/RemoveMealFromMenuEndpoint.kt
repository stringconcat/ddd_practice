package com.stringconcat.ddd.shop.rest.menu

import com.stringconcat.ddd.common.rest.noContent
import com.stringconcat.ddd.common.rest.resourceNotFound
import com.stringconcat.ddd.shop.domain.menu.MealId
import com.stringconcat.ddd.shop.rest.API_V1_MENU_DELETE_BY_ID
import com.stringconcat.ddd.shop.usecase.menu.RemoveMealFromMenu
import com.stringconcat.ddd.shop.usecase.menu.RemoveMealFromMenuUseCaseError
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class RemoveMealFromMenuEndpoint(private val removeMealFromMenu: RemoveMealFromMenu) {

    @DeleteMapping(path = [API_V1_MENU_DELETE_BY_ID])
    fun execute(@PathVariable("id") mealId: Long) =
        removeMealFromMenu.execute(MealId(mealId))
            .fold({
                it.toRestError()
            }, {
                noContent()
            })
}

fun RemoveMealFromMenuUseCaseError.toRestError() =
    when (this) {
        is RemoveMealFromMenuUseCaseError.MealNotFound -> resourceNotFound()
    }
