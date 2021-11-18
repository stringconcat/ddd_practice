package com.stringconcat.ddd.shop.rest.order

import com.stringconcat.ddd.common.rest.noContent
import com.stringconcat.ddd.common.rest.resourceNotFound
import com.stringconcat.ddd.common.rest.restBusinessError
import com.stringconcat.ddd.shop.domain.order.ShopOrderId
import com.stringconcat.ddd.shop.rest.API_V1_ORDER_CANCEL_BY_ID
import com.stringconcat.ddd.shop.usecase.order.CancelOrder
import com.stringconcat.ddd.shop.usecase.order.CancelOrderUseCaseError
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class CancelOrderEndpoint(private val cancelOrder: CancelOrder) {

    @PutMapping(path = [API_V1_ORDER_CANCEL_BY_ID])
    fun execute(@PathVariable("id") id: Long) = cancelOrder.execute(ShopOrderId(id))
        .fold({ it.toRestError() }, { noContent() })

    private fun CancelOrderUseCaseError.toRestError() =
        when (this) {
            CancelOrderUseCaseError.InvalidOrderState,
            -> restBusinessError(title = "Invalid state", code = "invalid_state")
            CancelOrderUseCaseError.OrderNotFound -> resourceNotFound()
        }
}