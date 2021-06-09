package com.stringconcat.ddd.kitchen.web.order

import com.stringconcat.ddd.kitchen.domain.order.KitchenOrderId
import com.stringconcat.ddd.kitchen.usecase.order.CookOrder
import com.stringconcat.ddd.kitchen.usecase.order.GetOrders
import com.stringconcat.ddd.kitchen.web.URLs
import com.stringconcat.ddd.kitchen.web.Views
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class KitchenOrderController(
    private val getOrders: GetOrders,
    private val cookOrder: CookOrder
) {

    companion object {
        const val ORDERS_ATTRIBUTE = "orders"
        const val ERROR_ATTRIBUTE = "error"
    }

    @GetMapping(URLs.kitchen_orders)
    fun orders(map: ModelMap): String {
        map.addAttribute(ORDERS_ATTRIBUTE, getOrders.execute())
        return Views.kitchenOrders
    }

    @PostMapping(URLs.cook_kitchen_order)
    fun confirm(@RequestParam orderId: Long, map: ModelMap): String {
        cookOrder.execute(KitchenOrderId(orderId)).mapLeft {
            map.addAttribute(ORDERS_ATTRIBUTE, getOrders.execute())
            map.addAttribute(ERROR_ATTRIBUTE, it.message)
            return@confirm Views.kitchenOrders
        }

        return "redirect:${URLs.kitchen_orders}"
    }
}