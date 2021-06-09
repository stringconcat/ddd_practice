package com.stringconcat.ddd.shop.web.order

import com.stringconcat.ddd.shop.domain.order.ShopOrderId
import com.stringconcat.ddd.shop.domain.order.OrderState
import com.stringconcat.ddd.shop.usecase.order.CancelOrder
import com.stringconcat.ddd.shop.usecase.order.ConfirmOrder
import com.stringconcat.ddd.shop.usecase.order.GetOrders
import com.stringconcat.ddd.shop.web.URLs
import com.stringconcat.ddd.shop.web.Views
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class ShopOrderController(
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

    @GetMapping(URLs.shop_orders)
    fun orders(map: ModelMap): String {
        map.addAttribute(ORDERS_ATTRIBUTE, getOrdersUseCase.execute())
        map.addAttribute(STATUS_NAMES_ATTRIBUTE, statusNames)
        return Views.shopOrders
    }

    @PostMapping(URLs.confirm_shop_order)
    fun confirm(@RequestParam orderId: Long, map: ModelMap): String {
        confirmOrder.execute(ShopOrderId(orderId)).mapLeft {
            map.addAttribute(ORDERS_ATTRIBUTE, getOrdersUseCase.execute())
            map.addAttribute(STATUS_NAMES_ATTRIBUTE, statusNames)
            map.addAttribute(ERROR_ATTRIBUTE, it.message)
            return@confirm Views.shopOrders
        }

        return "redirect:${URLs.shop_orders}"
    }

    @PostMapping(URLs.cancel_shop_order)
    fun cancel(@RequestParam orderId: Long, map: ModelMap): String {
        cancelOrder.execute(ShopOrderId(orderId)).mapLeft {
            map.addAttribute(ORDERS_ATTRIBUTE, getOrdersUseCase.execute())
            map.addAttribute(STATUS_NAMES_ATTRIBUTE, statusNames)
            map.addAttribute(ERROR_ATTRIBUTE, it.message)
            return@cancel Views.shopOrders
        }

        return "redirect:${URLs.shop_orders}"
    }
}