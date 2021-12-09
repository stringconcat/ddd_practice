package com.stringconcat.ddd.kitchen.app

import arrow.core.Either
import com.stringconcat.ddd.kitchen.usecase.order.CreateOrder
import com.stringconcat.ddd.kitchen.usecase.order.CreateOrderRequest
import com.stringconcat.ddd.kitchen.usecase.order.CreateOrderUseCaseError
import io.kotest.matchers.shouldBe

class MockCreateOrder : CreateOrder {
    lateinit var response: Either<CreateOrderUseCaseError, Unit>
    lateinit var request: CreateOrderRequest

    override fun execute(request: CreateOrderRequest): Either<CreateOrderUseCaseError, Unit> {
        this.request = request
        return response
    }

    fun verifyInvoked(request: CreateOrderRequest) {
        this.request shouldBe request
    }
}