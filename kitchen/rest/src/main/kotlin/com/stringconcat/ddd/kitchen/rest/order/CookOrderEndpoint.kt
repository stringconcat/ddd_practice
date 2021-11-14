package com.stringconcat.ddd.kitchen.rest.order

import com.stringconcat.ddd.common.rest.noContent
import com.stringconcat.ddd.common.rest.resourceNotFound
import com.stringconcat.ddd.kitchen.domain.order.KitchenOrderId
import com.stringconcat.ddd.kitchen.usecase.order.CookOrder
import com.stringconcat.ddd.kitchen.usecase.order.CookOrderUseCaseError
import com.stringconcat.ddd.kitchen.rest.API_V1_ORDER
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class CookOrderEndpoint(private val cookOrder: CookOrder) {
    @PutMapping(path = ["$API_V1_ORDER/{id}/cook"])
    fun execute(@PathVariable("id") id: Long) = cookOrder.execute(KitchenOrderId(id))
        .fold({ it.toRestError() }, { noContent() })

    private fun CookOrderUseCaseError.toRestError() =
        when (this) {
            CookOrderUseCaseError.OrderNotFound -> resourceNotFound()
        }
}