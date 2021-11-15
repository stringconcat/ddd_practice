package com.stringconcat.ddd.kitchen.rest.order

import com.stringconcat.ddd.kitchen.domain.order.OrderItem
import com.stringconcat.ddd.kitchen.usecase.order.dto.OrderDetails
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.Relation
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn

@Relation(collectionRelation = "orders")
data class OrderModel(
    val id: Long,
    val cooked: Boolean,
    val meals: List<OrderItemModel>,
) : RepresentationModel<OrderModel>()

data class OrderItemModel(val meal: String, val count: Int)

fun OrderDetails.toOrderModel(): OrderModel {
    return OrderModel(
        id = id.value,
        cooked = cooked,
        meals = meals.toModel()
    ).add(
        linkTo(
            methodOn(GetOrdersEndpoint::class.java)
                .execute()
        ).withSelfRel()
    )
}

fun List<OrderItem>.toModel() =
    this.map { OrderItemModel(meal = it.meal.value, count = it.count.value) }
