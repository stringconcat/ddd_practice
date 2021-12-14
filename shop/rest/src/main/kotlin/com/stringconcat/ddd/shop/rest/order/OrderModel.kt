package com.stringconcat.ddd.shop.rest.order

import com.stringconcat.ddd.common.types.common.Address
import com.stringconcat.ddd.shop.usecase.order.dto.OrderDetails
import com.stringconcat.ddd.shop.usecase.order.dto.OrderItemDetails
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.Relation
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.afford
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity

@Relation(collectionRelation = "orders")
data class OrderModel(
    val id: Long,
    val address: AddressModel,
    val items: List<OrderItemModel>,
    val totalPrice: String,
    val version: Long,
) : RepresentationModel<OrderModel>()

data class OrderItemModel(val mealId: Long, val count: Int)

fun OrderDetails.toOrderModel(): OrderModel {
    val result = OrderModel(id = this.id.toLongValue(),
        totalPrice = this.total.toStringValue(),
        address = this.address.toModel(),
        items = this.items.toModel(),
        version = this.version.toLongValue()
    ).add(linkTo(methodOn(GetOrderByIdEndpoint::class.java)
        .execute(this.id.toLongValue())).withSelfRel())

    if (this.readyForConfirmOrCancel) {
        result.add(linkTo(methodOn(CancelOrderEndpoint::class.java)
            .execute(this.id.toLongValue())).withRel("cancel")
            .andAffordance(afford(methodOn(CancelOrderEndpoint::class.java)
                .execute(this.id.toLongValue()))))
            .add(linkTo(methodOn(ConfirmOrderEndpoint::class.java)
                .execute(this.id.toLongValue())).withRel("confirm")
                .andAffordance(afford(methodOn(ConfirmOrderEndpoint::class.java)
                    .execute(this.id.toLongValue()))))
    }
    return result
}

fun OrderDetails.toOrderModelEntity(): ResponseEntity<OrderModel> = ResponseEntity.ok(this.toOrderModel())

fun List<OrderItemDetails>.toModel() =
    this.map { OrderItemModel(mealId = it.mealId.toLongValue(), count = it.count.toIntValue()) }

fun Address.toModel() = AddressModel(
    street = this.streetToStringValue(),
    building = this.buildingToIntValue())

data class AddressModel(val street: String, val building: Int)
