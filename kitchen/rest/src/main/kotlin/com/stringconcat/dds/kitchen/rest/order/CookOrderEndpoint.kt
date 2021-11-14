package com.stringconcat.dds.kitchen.rest.order

import com.stringconcat.ddd.kitchen.domain.order.KitchenOrderId
import com.stringconcat.ddd.kitchen.usecase.order.CookOrder
import com.stringconcat.ddd.kitchen.usecase.order.CookOrderUseCaseError
import com.stringconcat.dds.kitchen.rest.API_V1_ORDER
import com.stringconcat.dds.kitchen.rest.noContent
import com.stringconcat.dds.kitchen.rest.resourceNotFound
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