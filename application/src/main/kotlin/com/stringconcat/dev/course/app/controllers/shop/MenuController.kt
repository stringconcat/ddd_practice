package com.stringconcat.dev.course.app.controllers.shop

import arrow.core.flatMap
import com.stringconcat.ddd.shop.domain.menu.MealId
import com.stringconcat.ddd.shop.usecase.menu.AddMealToMenu
import com.stringconcat.ddd.shop.usecase.menu.AddMealToMenuRequest
import com.stringconcat.ddd.shop.usecase.menu.GetMenu
import com.stringconcat.ddd.shop.usecase.menu.RemoveMealFromMenu
import com.stringconcat.dev.course.app.controllers.URLs
import com.stringconcat.dev.course.app.controllers.Views
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import java.math.BigDecimal

@Controller
@RequestMapping()
class MenuController(
    private val addMealToMenuUseCase: AddMealToMenu,
    private val removeMealFromMenuUseCase: RemoveMealFromMenu,
    private val getMenuUseCase: GetMenu
) {

    companion object {
        const val ERROR_ATTRIBUTE = "error"
        const val MENU_ATTRIBUTE = "menu"
    }

    @GetMapping(URLs.listMenu)
    fun menu(modelMap: ModelMap): String {
        modelMap.addAttribute(MENU_ATTRIBUTE, getMenuUseCase.execute())
        return Views.menu
    }

    @PostMapping(URLs.addMeal)
    fun addMealToMenu(
        @RequestParam name: String,
        @RequestParam description: String,
        @RequestParam price: BigDecimal,
        modelMap: ModelMap
    ): String {

        AddMealToMenuRequest.from(name, description, price)
            .mapLeft { it.message }
            .flatMap {
                addMealToMenuUseCase.execute(it)
                    .mapLeft { it.message }
            }.mapLeft {
                modelMap.addAttribute(MENU_ATTRIBUTE, getMenuUseCase.execute())
                modelMap.addAttribute(ERROR_ATTRIBUTE, it)
                return@addMealToMenu Views.menu
            }

        return "redirect:/${Views.menu}"
    }

    @PostMapping(URLs.removeMeal)
    fun removeMealFromMenu(@RequestParam id: Long, modelMap: ModelMap): String {
        removeMealFromMenuUseCase.execute(MealId(id)).mapLeft {
            modelMap.addAttribute(MENU_ATTRIBUTE, getMenuUseCase.execute())
            modelMap.addAttribute(ERROR_ATTRIBUTE, it.message)
            return@removeMealFromMenu Views.menu
        }
        return "redirect:/${Views.menu}"
    }
}