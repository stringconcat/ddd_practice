package com.stringconcat.ddd.shop.rest.order

import com.stringconcat.ddd.common.rest.resourceNotFound
import com.stringconcat.ddd.shop.domain.order.ShopOrderId
import com.stringconcat.ddd.shop.rest.API_V1_ORDER_GET_BY_ID
import com.stringconcat.ddd.shop.usecase.order.GetOrderById
import com.stringconcat.ddd.shop.usecase.order.GetOrderByIdUseCaseError
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class GetOrderByIdEndpoint(private val getOrderById: GetOrderById) {

    @GetMapping(path = [API_V1_ORDER_GET_BY_ID])
    fun execute(@PathVariable("id") id: Long) =
        getOrderById.execute(ShopOrderId(id))
            .fold({ it.toRestError() },
                { it.toOrderModelEntity() })
}

fun GetOrderByIdUseCaseError.toRestError() =
    when (this) {
        is GetOrderByIdUseCaseError.OrderNotFound -> resourceNotFound()
    }