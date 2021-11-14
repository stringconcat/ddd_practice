package com.stringconcat.ddd.shop.rest.order

import com.stringconcat.ddd.common.rest.resourceNotFound
import com.stringconcat.ddd.common.types.common.Address
import com.stringconcat.ddd.shop.domain.order.ShopOrderId
import com.stringconcat.ddd.shop.rest.API_V1_ORDER
import com.stringconcat.ddd.shop.usecase.order.GetOrderById
import com.stringconcat.ddd.shop.usecase.order.GetOrderByIdUseCaseError
import com.stringconcat.ddd.shop.usecase.order.OrderDetails
import com.stringconcat.ddd.shop.usecase.order.OrderItemDetails
import org.springframework.hateoas.RepresentationModel
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class GetOrderByIdEndpoint(private val getOrderById: GetOrderById) {

    @GetMapping(path = ["$API_V1_ORDER/{id}"])
    fun execute(@PathVariable("id") id: Long) =
        getOrderById.execute(ShopOrderId(id))
            .fold({ it.toRestError() },
                { OrderModel.from(it) })
}

data class OrderModel(
    val id: Long,
    val address: AddressModel,
    val items: List<OrderItemModel>,
    val totalPrice: String,
    val version: Long,
) : RepresentationModel<OrderModel>() {

    companion object {
        fun from(orderDetails: OrderDetails) =
            ResponseEntity.ok(
                OrderModel(id = orderDetails.id.value,
                    totalPrice = orderDetails.total.value.toString(),
                    address = orderDetails.address.toModel(),
                    items = orderDetails.items.toModel(),
                    version = orderDetails.version.value
                )
            )
    }
}

fun Address.toModel() = AddressModel(street = this.street, building = this.building)
data class AddressModel(val street: String, val building: Int)

data class OrderItemModel(val mealId: Long, val count: Int)

fun List<OrderItemDetails>.toModel() =
    this.map { OrderItemModel(mealId = it.mealId.value, count = it.count.value) }

fun GetOrderByIdUseCaseError.toRestError() =
    when (this) {
        is GetOrderByIdUseCaseError.OrderNotFound -> resourceNotFound()
    }