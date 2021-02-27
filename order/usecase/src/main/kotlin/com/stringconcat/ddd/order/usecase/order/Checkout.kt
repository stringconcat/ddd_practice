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

sealed class CheckoutUseCaseError(open val message: String) {
    object CartNotFound : CheckoutUseCaseError("Cart not found")
    object EmptyCart : CheckoutUseCaseError("Empty cart")
    object AlreadyHasActiveOrder : CheckoutUseCaseError("Already has active order")
    data class InvalidAddress(override val message: String) : CheckoutUseCaseError(message)
}
