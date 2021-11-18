package com.stringconcat.ddd.kitchen.rest.order

import com.stringconcat.ddd.kitchen.rest.API_V1_ORDERS_GET_ALL
import com.stringconcat.ddd.kitchen.usecase.order.GetOrders
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class GetOrdersEndpoint(private val getOrders: GetOrders) {
    @GetMapping(path = [API_V1_ORDERS_GET_ALL])
    fun execute(): ResponseEntity<CollectionModel<OrderModel>> {
        val ordersModel = getOrders.execute().map { it.toOrderModel() }
        val collectionModel = CollectionModel.of(ordersModel)
            .add(
                WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(GetOrdersEndpoint::class.java)
                        .execute()
                ).withSelfRel()
            )
        return ResponseEntity.ok(collectionModel)
    }
}