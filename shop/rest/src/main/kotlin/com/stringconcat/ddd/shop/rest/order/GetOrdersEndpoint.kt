package com.stringconcat.ddd.shop.rest.order

import arrow.core.nel
import com.stringconcat.ddd.shop.domain.order.ShopOrderId
import com.stringconcat.ddd.shop.rest.API_V1_ORDER
import com.stringconcat.ddd.shop.rest.ValidationError
import com.stringconcat.ddd.shop.rest.toInvalidParamsBadRequest
import com.stringconcat.ddd.shop.usecase.order.GetOrders
import com.stringconcat.ddd.shop.usecase.order.GetOrdersUseCaseError
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class GetOrdersEndpoint(private val getOrders: GetOrders) {

    @GetMapping(path = [API_V1_ORDER])
    fun execute(@RequestParam("startId") startId: Long, @RequestParam("limit") limit: Int) =
        getOrders.execute(ShopOrderId(startId), limit + 1)
            .fold({ it.toRestError() },
                { it.toPagedModelResponse(limit) })
}

fun GetOrdersUseCaseError.toRestError() =
    when (this) {
        is GetOrdersUseCaseError.LimitExceed ->
            ValidationError("Max limit is ${this.maxSize - 1}")
                .nel().toInvalidParamsBadRequest()
    }