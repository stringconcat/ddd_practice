package com.stringconcat.ddd.shop.rest.order

import com.stringconcat.ddd.common.types.common.Address
import com.stringconcat.ddd.shop.rest.CursorPagedModel
import com.stringconcat.ddd.shop.usecase.order.dto.OrderDetails
import com.stringconcat.ddd.shop.usecase.order.dto.OrderItemDetails
import org.springframework.hateoas.RepresentationModel
import org.springframework.http.ResponseEntity

data class OrderModel(
    val id: Long,
    val address: AddressModel,
    val items: List<OrderItemModel>,
    val totalPrice: String,
    val version: Long,
) : RepresentationModel<OrderModel>()

fun OrderDetails.toOrderModel() {
    ResponseEntity.ok(
        OrderModel(id = this.id.value,
            totalPrice = this.total.value.toString(),
            address = this.address.toModel(),
            items = this.items.toModel(),
            version = this.version.value
        )
    )
}

data class OrderItemModel(val mealId: Long, val count: Int)

fun List<OrderItemDetails>.toModel() =
    this.map { OrderItemModel(mealId = it.mealId.value, count = it.count.value) }

fun List<OrderDetails>.toPagedModel(originalLimit: Int): ResponseEntity<*> {
    return if (size > originalLimit) {
        ResponseEntity.ok(CursorPagedModel.from(
            this.subList(0, originalLimit).map { it.toOrderModel() })
        )
    } else {
        ResponseEntity.ok(CursorPagedModel.from(this.map { it.toOrderModel() }))
    }
}

fun Address.toModel() = AddressModel(street = this.street, building = this.building)
data class AddressModel(val street: String, val building: Int)
