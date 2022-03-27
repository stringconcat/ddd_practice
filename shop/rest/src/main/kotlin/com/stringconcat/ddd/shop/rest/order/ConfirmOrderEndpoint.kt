package com.stringconcat.ddd.shop.rest.order

import com.stringconcat.ddd.common.rest.noContent
import com.stringconcat.ddd.common.rest.resourceNotFound
import com.stringconcat.ddd.common.rest.restBusinessError
import com.stringconcat.ddd.shop.domain.order.ShopOrderId
import com.stringconcat.ddd.shop.rest.API_V1_ORDER_CONFIRM_BY_ID
import com.stringconcat.ddd.shop.usecase.order.ConfirmOrder
import com.stringconcat.ddd.shop.usecase.order.ConfirmOrderUseCaseError
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Api(tags = ["Order"])
class ConfirmOrderEndpoint(private val confirmOrder: ConfirmOrder) {

    @ApiOperation("Confirm an order")
    @PutMapping(path = [API_V1_ORDER_CONFIRM_BY_ID])
    fun execute(@PathVariable("id") id: Long) = confirmOrder.execute(ShopOrderId(id))
        .fold({ it.toRestError() }, { noContent() })
}

private fun ConfirmOrderUseCaseError.toRestError() =
    when (this) {
        ConfirmOrderUseCaseError.InvalidOrderState,
        -> restBusinessError(title = "Invalid state", code = "invalid_state")
        ConfirmOrderUseCaseError.OrderNotFound -> resourceNotFound()
    }
