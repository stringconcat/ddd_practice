package com.stringconcat.ddd.shop.rest.order

import com.stringconcat.ddd.common.types.common.Address
import com.stringconcat.ddd.shop.usecase.order.dto.OrderDetails
import com.stringconcat.ddd.shop.usecase.order.dto.OrderItemDetails
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.Relation
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
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
    val result = OrderModel(id = this.id.value,
        totalPrice = this.total.toStringValue(),
        address = this.address.toModel(),
        items = this.items.toModel(),
        version = this.version.value
    ).add(linkTo(methodOn(GetOrderByIdEndpoint::class.java)
        .execute(this.id.value)).withSelfRel())

    if (this.readyForConfirmOrCancel) {
        result.add(linkTo(methodOn(CancelOrderEndpoint::class.java)
            .execute(this.id.value)).withRel("cancel"))
            .add(linkTo(methodOn(ConfirmOrderEndpoint::class.java)
                .execute(this.id.value)).withRel("confirm"))
    }
    return result
}

fun OrderDetails.toOrderModelEntity(): ResponseEntity<OrderModel> = ResponseEntity.ok(this.toOrderModel())

fun List<OrderItemDetails>.toModel() =
    this.map { OrderItemModel(mealId = it.mealId.value, count = it.count.toIntValue()) }

fun Address.toModel() = AddressModel(
    street = this.streetToStringValue(),
    building = this.buildingToIntValue())
data class AddressModel(val street: String, val building: Int)
