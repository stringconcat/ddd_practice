package com.stringconcat.ddd.kitchen.rest.order

import com.stringconcat.ddd.common.rest.resourceNotFound
import com.stringconcat.ddd.kitchen.domain.order.KitchenOrderId
import com.stringconcat.ddd.kitchen.rest.API_V1_ORDER
import com.stringconcat.ddd.kitchen.usecase.order.GetOrderById
import com.stringconcat.ddd.kitchen.usecase.order.GetOrderByIdUseCaseError
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class GetOrderByIdEndpoint(private val getOrderById: GetOrderById) {

    @GetMapping(path = ["$API_V1_ORDER/{id}"])
    fun execute(@PathVariable("id") id: Long) =
        getOrderById.execute(KitchenOrderId(id))
            .fold({ it.toRestError() },
                { it.toOrderModelEntity() })
}

fun GetOrderByIdUseCaseError.toRestError() =
    when (this) {
        is GetOrderByIdUseCaseError.OrderNotFound -> resourceNotFound()
    }