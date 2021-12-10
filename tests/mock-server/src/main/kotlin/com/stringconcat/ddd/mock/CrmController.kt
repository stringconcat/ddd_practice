package com.stringconcat.ddd.mock

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal

@RestController
class CrmController {

    var lastOrderId = -1L
    var result = Result.SUCCESS

    @PostMapping("/orders")
    fun importOrder(@RequestBody request: Request): Response {
        lastOrderId = request.id
        return Response(result)
    }

    @GetMapping("/order")
    fun getLastOrderId(): OrderId {
        return OrderId(lastOrderId)
    }
}

data class Request(val id: Long, val customerId: String, val totalPrice: BigDecimal)
data class Response(val result: Result)
enum class Result { SUCCESS, ALREADY_EXISTS }

data class OrderId(val id: Long)