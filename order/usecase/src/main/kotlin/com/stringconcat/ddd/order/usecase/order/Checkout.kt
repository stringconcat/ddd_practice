package com.stringconcat.ddd.order.usecase.order

import arrow.core.Either
import com.stringconcat.ddd.order.domain.menu.Price
import com.stringconcat.ddd.order.domain.order.CustomerOrderId
import java.net.URL

interface Checkout {
    fun execute(request: CheckoutRequest): Either<CheckoutUseCaseError, PaymentInfo>
}

data class PaymentInfo(
    val orderId: CustomerOrderId,
    val price: Price,
    val paymentURL: URL
)

data class CheckoutRequest(val customerId: String, val address: Address) {
    data class Address(val street: String, val building: Int)
}

sealed class CheckoutUseCaseError {
    object CartNotFound : CheckoutUseCaseError()
    object EmptyCart : CheckoutUseCaseError()
    object AlreadyHasActiveOrder : CheckoutUseCaseError()
    data class InvalidAddress(val message: String) : CheckoutUseCaseError()
}
