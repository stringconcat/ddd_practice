package com.stringconcat.dev.course.app.order

import com.stringconcat.ddd.order.usecase.menu.AddMealToMenuRequest
import com.stringconcat.ddd.order.usecase.menu.AddMealToMenuUseCase
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
    private val addMealToMenuUseCase: AddMealToMenuUseCase,
    private val removeMealFromMenuUseCase: RemoveMealFromMenuUseCase,
    private val getMenuUseCase: GetMenuUseCase
) {

    companion object {
        const val ERROR_ATTRIBUTE = "error"
        const val MENU_ATTRIBUTE = "menu"
    }

    @GetMapping(URLs.listMenu)
    fun menu(modelMap: ModelMap): String {
        modelMap.addAttribute(MENU_ATTRIBUTE, getMenuUseCase.getMenu())
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
        addMealToMenuUseCase.addMealToMenu(request).mapLeft {
            modelMap.addAttribute(MENU_ATTRIBUTE, getMenuUseCase.getMenu())
            modelMap.addAttribute(ERROR_ATTRIBUTE, it.message)
            return@addMealToMenu Views.menu
        }

        return "redirect:/${Views.menu}"
    }

    @PostMapping(URLs.removeMeal)
    fun removeMealFromMenu(@RequestParam id: Long, modelMap: ModelMap): String {
        removeMealFromMenuUseCase.removeMealFromMenu(id).mapLeft {
            modelMap.addAttribute(ERROR_ATTRIBUTE, it.message)
        }
        return "redirect:/${Views.menu}"
    }
}