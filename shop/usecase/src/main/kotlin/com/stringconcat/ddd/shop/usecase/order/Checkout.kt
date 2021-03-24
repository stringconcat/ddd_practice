package com.stringconcat.ddd.shop.usecase.order

import arrow.core.Either
import com.stringconcat.ddd.common.types.common.Address
import com.stringconcat.ddd.shop.domain.cart.CustomerId
import com.stringconcat.ddd.shop.domain.menu.Price
import com.stringconcat.ddd.shop.domain.order.CustomerOrderId
import java.net.URL

interface Checkout {
    fun execute(request: CheckoutRequest): Either<CheckoutUseCaseError, PaymentInfo>
}

data class PaymentInfo(
    val orderId: CustomerOrderId,
    val price: Price,
    val paymentURL: URL
)

data class CheckoutRequest(
    val forCustomer: CustomerId,
    val deliveryTo: Address
)

sealed class CheckoutUseCaseError(open val message: String) {
    object CartNotFound : CheckoutUseCaseError("Cart not found")
    object EmptyCart : CheckoutUseCaseError("Empty cart")
    object AlreadyHasActiveOrder : CheckoutUseCaseError("Already has active order")
    data class InvalidAddress(override val message: String) : CheckoutUseCaseError(message)
}
