package com.stringconcat.ddd.order.usecase.order

import arrow.core.Either
import arrow.core.extensions.either.apply.tupled
import arrow.core.flatMap
import arrow.core.rightIfNotNull
import com.stringconcat.ddd.common.types.common.Address
import com.stringconcat.ddd.common.types.common.CreateAddressError
import com.stringconcat.ddd.order.domain.cart.CustomerId
import com.stringconcat.ddd.order.domain.menu.Price
import com.stringconcat.ddd.order.domain.order.CheckoutError
import com.stringconcat.ddd.order.domain.order.CustomerOrder
import com.stringconcat.ddd.order.domain.order.CustomerOrderId
import com.stringconcat.ddd.order.domain.order.CustomerOrderIdGenerator
import com.stringconcat.ddd.order.domain.providers.MealPriceProvider
import com.stringconcat.ddd.order.domain.rules.CustomerHasActiveOrderRule
import com.stringconcat.ddd.order.usecase.cart.CartExtractor
import java.net.URL

class CheckoutUseCase(
    private val idGenerator: CustomerOrderIdGenerator,
    private val cartExtractor: CartExtractor,
    private val activeOrderRule: CustomerHasActiveOrderRule,
    private val priceProvider: MealPriceProvider,
    private val paymentUrlProvider: PaymentUrlProvider,
    private val customerOrderPersister: CustomerOrderPersister

) {

    fun execute(request: CheckoutRequest): Either<CheckoutUseCaseError, PaymentInfo> {

        return tupled(

            Address.from(
                street = request.address.street,
                building = request.address.building
            ).mapLeft { it.toError() },

            cartExtractor.getCart(forCustomer = CustomerId(request.customerId))
                .rightIfNotNull { CheckoutUseCaseError.CartNotFound }

        ).flatMap {

            CustomerOrder.checkout(
                idGenerator = idGenerator,
                activeOrder = activeOrderRule,
                priceProvider = priceProvider,
                address = it.a,
                cart = it.b
            ).mapLeft { err -> err.toError() }
        }.map { order ->

            val url = paymentUrlProvider.provideUrl(order.id, order.totalPrice())
            val totalPrice = order.totalPrice()
            customerOrderPersister.save(order)
            PaymentInfo(
                orderId = order.id,
                price = totalPrice,
                paymentURL = url
            )
        }
    }
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

fun CreateAddressError.toError(): CheckoutUseCaseError {
    return when (this) {
        CreateAddressError.EmptyString -> CheckoutUseCaseError.InvalidAddress("Empty street")
        CreateAddressError.NonPositiveBuilding -> CheckoutUseCaseError.InvalidAddress("Negative value")
    }
}

fun CheckoutError.toError(): CheckoutUseCaseError {
    return when (this) {
        CheckoutError.AlreadyHasActiveOrder -> CheckoutUseCaseError.AlreadyHasActiveOrder
        CheckoutError.EmptyCart -> CheckoutUseCaseError.EmptyCart
    }
}