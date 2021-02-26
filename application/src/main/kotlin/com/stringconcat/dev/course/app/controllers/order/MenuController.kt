package com.stringconcat.dev.course.app.controllers.order

import com.stringconcat.ddd.order.usecase.menu.AddMealToMenu
import com.stringconcat.ddd.order.usecase.menu.AddMealToMenuRequest
import com.stringconcat.ddd.order.usecase.menu.GetMenuUseCase
import com.stringconcat.ddd.order.usecase.menu.RemoveMealFromMenuUseCase
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
    private val removeMealFromMenuUseCase: RemoveMealFromMenuUseCase,
    private val getMenuUseCase: GetMenuUseCase
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
        val request = AddMealToMenuRequest(name, description, price)
        addMealToMenuUseCase.execute(request).mapLeft {
            modelMap.addAttribute(MENU_ATTRIBUTE, getMenuUseCase.execute())
            modelMap.addAttribute(ERROR_ATTRIBUTE, it.message)
            return@addMealToMenu Views.menu
        }

        return "redirect:/${Views.menu}"
    }

    @PostMapping(URLs.removeMeal)
    fun removeMealFromMenu(@RequestParam id: Long, modelMap: ModelMap): String {
        removeMealFromMenuUseCase.execute(id).mapLeft {
            modelMap.addAttribute(MENU_ATTRIBUTE, getMenuUseCase.execute())
            modelMap.addAttribute(ERROR_ATTRIBUTE, it.message)
            return@removeMealFromMenu Views.menu
        }
        return "redirect:/${Views.menu}"
    }
}