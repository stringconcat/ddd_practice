package com.stringconcat.ddd.shop.app.controllers

import com.stringconcat.ddd.shop.domain.order.ShopOrderId
import com.stringconcat.ddd.shop.usecase.order.PayOrder
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequestMapping(URLs.payment)
class PaymentController(private val payOrder: PayOrder) {

    companion object {
        const val ERROR_ATTRIBUTE = "error"
    }

    @GetMapping
    fun showOrderInfo(@RequestParam orderId: Long, map: ModelMap): String {
        map.addAttribute("orderId", orderId)
        return Views.payment
    }

    @PostMapping
    fun payOrder(@RequestParam orderId: Long, map: ModelMap): String {
        map.addAttribute("orderId", orderId)
        payOrder.execute(ShopOrderId(orderId))
            .mapLeft { map.addAttribute(ERROR_ATTRIBUTE, it.message) }
        return Views.paymentResult
    }
}