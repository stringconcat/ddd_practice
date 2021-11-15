package com.stringconcat.ddd.kitchen.rest.order

import com.stringconcat.ddd.common.rest.CursorPagedModel
import com.stringconcat.ddd.kitchen.rest.API_V1_ORDER
import com.stringconcat.ddd.kitchen.usecase.order.GetOrders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class GetOrdersEndpoint(private val getOrders: GetOrders) {
    @GetMapping(path = [API_V1_ORDER])
    fun execute() = ResponseEntity.ok(CursorPagedModel.from(getOrders.execute().map { it.toOrderModel() }))
}