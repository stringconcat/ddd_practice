package com.stringconcat.ddd.shop.rest.order

import com.stringconcat.ddd.common.types.common.Address
import com.stringconcat.ddd.shop.usecase.order.dto.OrderDetails
import com.stringconcat.ddd.shop.usecase.order.dto.OrderItemDetails
import io.swagger.annotations.ApiModelProperty
import org.springframework.http.ResponseEntity

data class OrderModel(
    @ApiModelProperty("ID of the order") val id: Long,
    @ApiModelProperty("Address of the order") val address: AddressModel,
    @ApiModelProperty("A list of orders item") val items: List<OrderItemModel>,
    @ApiModelProperty("Total price of the order") val totalPrice: String,
    @ApiModelProperty("Version of the order") val version: Long,
    @ApiModelProperty("Flag of readiness for order cancellation or confirmation") val readyForConfirmOrCancel: Boolean
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
