package com.stringconcat.ddd.shop.rest.order

import arrow.core.nel
import com.stringconcat.ddd.shop.domain.order.ShopOrderId
import com.stringconcat.ddd.shop.rest.API_V1_ORDER
import com.stringconcat.ddd.shop.rest.CursorPagedModel
import com.stringconcat.ddd.shop.rest.ValidationError
import com.stringconcat.ddd.shop.rest.toInvalidParamsBadRequest
import com.stringconcat.ddd.shop.usecase.order.GetOrders
import com.stringconcat.ddd.shop.usecase.order.GetOrdersUseCaseError
import com.stringconcat.ddd.shop.usecase.order.ShopOrderInfo
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.Relation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class GetOrdersEndpoint(private val getOrders: GetOrders) {

    @GetMapping(path = [API_V1_ORDER])
    fun execute(@RequestParam("startId") startId: Long, @RequestParam("limit") limit: Int) =
        getOrders.execute(ShopOrderId(startId), limit + 1)
            .fold({ it.toRestError() },
                { it.toPagedModel(limit) })
}

@Relation(collectionRelation = "orders")
data class OrderInfoModel(val id: Long, val totalPrice: String) : RepresentationModel<OrderInfoModel>()

fun GetOrdersUseCaseError.toRestError() =
    when (this) {
        is GetOrdersUseCaseError.LimitExceed ->
            ValidationError("Max limit is ${this.maxSize - 1}")
                .nel().toInvalidParamsBadRequest()
    }

fun List<ShopOrderInfo>.toPagedModel(originalLimit: Int): ResponseEntity<*> {
    return if (size > originalLimit) {
        ResponseEntity.ok(CursorPagedModel.from(
            this.subList(0, originalLimit).map { it.toModel() })
        )
    } else {
        ResponseEntity.ok(CursorPagedModel.from(this.map { it.toModel() }))
    }
}

fun ShopOrderInfo.toModel() = OrderInfoModel(id = this.id.value, totalPrice = this.total.value.toString())