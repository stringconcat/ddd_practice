package com.stringconcat.ddd.shop.rest.order

import com.stringconcat.ddd.shop.domain.order.ShopOrderId
import com.stringconcat.ddd.shop.rest.API_V1_ORDER
import com.stringconcat.ddd.common.rest.noContent
import com.stringconcat.ddd.common.rest.resourceNotFound
import com.stringconcat.ddd.common.rest.restBusinessError
import com.stringconcat.ddd.shop.usecase.order.ConfirmOrder
import com.stringconcat.ddd.shop.usecase.order.ConfirmOrderUseCaseError
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ConfirmOrderEndpoint(private val confirmOrder: ConfirmOrder) {

    @PutMapping(path = ["$API_V1_ORDER/{id}/confirm"])
    fun execute(@PathVariable("id") id: Long) = confirmOrder.execute(ShopOrderId(id))
        .fold({ it.toRestError() }, { noContent() })
}

private fun ConfirmOrderUseCaseError.toRestError() =
    when (this) {
        ConfirmOrderUseCaseError.InvalidOrderState,
        -> restBusinessError(title = "Invalid state", code = "invalid_state")
        ConfirmOrderUseCaseError.OrderNotFound -> resourceNotFound()
    }
