package com.stringconcat.ddd.shop.rest.order

import arrow.core.nel
import com.stringconcat.ddd.common.rest.CursorPagedModel
import com.stringconcat.ddd.common.rest.ValidationError
import com.stringconcat.ddd.common.rest.toInvalidParamsBadRequest
import com.stringconcat.ddd.shop.domain.order.ShopOrderId
import com.stringconcat.ddd.shop.rest.API_V1_ORDER_GET_ALL
import com.stringconcat.ddd.shop.usecase.order.GetOrders
import com.stringconcat.ddd.shop.usecase.order.GetOrdersUseCaseError
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class GetOrdersEndpoint(private val getOrders: GetOrders) {

    @GetMapping(path = [API_V1_ORDER_GET_ALL])
    fun execute(@RequestParam("startId") startId: Long, @RequestParam("limit") limit: Int) =
            getOrders.execute(ShopOrderId(startId), limit + 1)
                    .fold({ it.toRestError() },
                            {
                                val list = it.map { o -> o.toOrderModel() }
                                val model = if (list.size > limit) {
                                    val nextId = list[limit].id
                                    CursorPagedModel(list.subList(0, limit), nextId)
                                } else {
                                    CursorPagedModel(list)
                                }
                                ResponseEntity.ok(model)
                            })
}

fun GetOrdersUseCaseError.toRestError() =
        when (this) {
            is GetOrdersUseCaseError.LimitExceed ->
                ValidationError("Max limit is ${this.maxSize - 1}")
                        .nel().toInvalidParamsBadRequest()
        }