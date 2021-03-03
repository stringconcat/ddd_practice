package com.stringconcat.dev.course.app.controllers.kitchen

import com.stringconcat.ddd.kitchen.domain.order.KitchenOrderId
import com.stringconcat.ddd.kitchen.usecase.order.CookOrder
import com.stringconcat.ddd.kitchen.usecase.order.GetOrders
import com.stringconcat.dev.course.app.controllers.URLs
import com.stringconcat.dev.course.app.controllers.Views
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class KitchenOrderController(
    private val getOrdersUseCase: GetOrders,
    private val cookOrder: CookOrder
) {

    companion object {
        const val ORDERS_ATTRIBUTE = "orders"
        const val ERROR_ATTRIBUTE = "error"
    }

    @GetMapping(URLs.kitchen_orders)
    fun orders(map: ModelMap): String {
        map.addAttribute(ORDERS_ATTRIBUTE, getOrdersUseCase.execute())
        return Views.kitchen_orders
    }

    @PostMapping(URLs.cook_kitchen_order)
    fun confirm(@RequestParam orderId: Long, map: ModelMap): String {
        cookOrder.execute(KitchenOrderId(orderId)).mapLeft {
            map.addAttribute(ORDERS_ATTRIBUTE, getOrdersUseCase.execute())
            map.addAttribute(ERROR_ATTRIBUTE, it.message)
            return@confirm Views.customer_orders
        }

        return "redirect:${URLs.kitchen_orders}"
    }
}