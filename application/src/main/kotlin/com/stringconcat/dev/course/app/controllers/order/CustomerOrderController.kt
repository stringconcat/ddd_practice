package com.stringconcat.dev.course.app.controllers.order

import com.stringconcat.ddd.order.domain.order.CustomerOrderId
import com.stringconcat.ddd.order.domain.order.OrderState
import com.stringconcat.ddd.order.usecase.order.CancelOrder
import com.stringconcat.ddd.order.usecase.order.ConfirmOrder
import com.stringconcat.ddd.order.usecase.order.GetOrders
import com.stringconcat.dev.course.app.controllers.URLs
import com.stringconcat.dev.course.app.controllers.Views
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class CustomerOrderController(
    private val getOrdersUseCase: GetOrders,
    private val confirmOrder: ConfirmOrder,
    private val cancelOrder: CancelOrder
) {

    companion object {
        const val ORDERS_ATTRIBUTE = "orders"
        const val ERROR_ATTRIBUTE = "error"
        const val STATUS_NAMES_ATTRIBUTE = "status_names"

        val statusNames = mapOf(
            OrderState.CANCELLED to "Cancelled",
            OrderState.COMPLETED to "Completed",
            OrderState.CONFIRMED to "Confirmed",
            OrderState.PAID to "Paid",
            OrderState.WAITING_FOR_PAYMENT to "Waiting for payment",
        )
    }

    @GetMapping(URLs.customer_orders)
    fun orders(map: ModelMap): String {
        map.addAttribute(ORDERS_ATTRIBUTE, getOrdersUseCase.execute())
        map.addAttribute(STATUS_NAMES_ATTRIBUTE, statusNames)
        return Views.customer_orders
    }

    @PostMapping(URLs.confirm_customer_order)
    fun confirm(@RequestParam orderId: Long, map: ModelMap): String {
        confirmOrder.execute(CustomerOrderId(orderId)).mapLeft {
            map.addAttribute(ORDERS_ATTRIBUTE, getOrdersUseCase.execute())
            map.addAttribute(STATUS_NAMES_ATTRIBUTE, statusNames)
            map.addAttribute(ERROR_ATTRIBUTE, it.message)
            return@confirm Views.customer_orders
        }

        return "redirect:${URLs.customer_orders}"
    }

    @PostMapping(URLs.cancel_customer_order)
    fun cancel(@RequestParam orderId: Long, map: ModelMap): String {
        cancelOrder.execute(CustomerOrderId(orderId)).mapLeft {
            map.addAttribute(ORDERS_ATTRIBUTE, getOrdersUseCase.execute())
            map.addAttribute(STATUS_NAMES_ATTRIBUTE, statusNames)
            map.addAttribute(ERROR_ATTRIBUTE, it.message)
            return@cancel Views.customer_orders
        }

        return "redirect:${URLs.customer_orders}"
    }
}