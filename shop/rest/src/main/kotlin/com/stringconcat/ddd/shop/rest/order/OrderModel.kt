package com.stringconcat.ddd.shop.rest.order

import com.stringconcat.ddd.common.types.common.Address
import com.stringconcat.ddd.shop.usecase.order.dto.OrderDetails
import com.stringconcat.ddd.shop.usecase.order.dto.OrderItemDetails
import io.swagger.annotations.ApiModelProperty
import org.springframework.http.ResponseEntity

data class OrderModel(
    @ApiModelProperty(notes = "ID of the order", name = "id") val id: Long,
    @ApiModelProperty(notes = "Address of the order", name = "address") val address: AddressModel,
    @ApiModelProperty(notes = "A list of orders item", name = "items") val items: List<OrderItemModel>,
    @ApiModelProperty(notes = "Total price of the order", name = "totalPrice") val totalPrice: String,
    @ApiModelProperty(notes = "Version of the order", name = "version") val version: Long,
    @ApiModelProperty(notes = "Flag of readiness for order cancellation or confirmation",
            name = "readyForConfirmOrCancel") val readyForConfirmOrCancel: Boolean
)

data class OrderItemModel(val mealId: Long, val count: Int)

fun OrderDetails.toOrderModel(): OrderModel {
    return OrderModel(
            id = id.toLongValue(),
            totalPrice = total.toStringValue(),
            address = address.toModel(),
            items = items.toModel(),
            version = version.toLongValue(),
            readyForConfirmOrCancel = readyForConfirmOrCancel
    )
}

fun OrderDetails.toOrderModelEntity(): ResponseEntity<OrderModel> = ResponseEntity.ok(this.toOrderModel())

fun List<OrderItemDetails>.toModel() =
        this.map { OrderItemModel(mealId = it.mealId.toLongValue(), count = it.count.toIntValue()) }

fun Address.toModel() = AddressModel(
        street = this.streetToStringValue(),
        building = this.buildingToIntValue())

data class AddressModel(val street: String, val building: Int)
