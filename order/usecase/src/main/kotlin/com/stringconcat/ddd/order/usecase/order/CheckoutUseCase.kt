package com.stringconcat.ddd.order.usecase.order

import arrow.core.Either
import arrow.core.extensions.either.apply.tupled
import arrow.core.flatMap
import arrow.core.rightIfNotNull
import com.stringconcat.ddd.common.types.common.Address
import com.stringconcat.ddd.common.types.common.CreateAddressError
import com.stringconcat.ddd.order.domain.cart.CustomerCartExtractor
import com.stringconcat.ddd.order.domain.cart.CustomerId
import com.stringconcat.ddd.order.domain.order.CheckoutError
import com.stringconcat.ddd.order.domain.order.CustomerOrder
import com.stringconcat.ddd.order.domain.order.CustomerOrderId
import com.stringconcat.ddd.order.domain.order.CustomerOrderIdGenerator
import com.stringconcat.ddd.order.domain.providers.MealPriceProvider
import com.stringconcat.ddd.order.domain.rules.CustomerHasActiveOrderRule

class CheckoutUseCase(
    private val idGenerator: CustomerOrderIdGenerator,
    private val customerCartExtractor: CustomerCartExtractor,
    private val activeOrderRule: CustomerHasActiveOrderRule,
    private val priceProvider: MealPriceProvider,
    private val customerOrderPersister: CustomerOrderPersister

) {

    fun checkout(request: CheckoutRequest): Either<CheckoutUseCaseError, CustomerOrderId> {

        return tupled(

            Address.from(
                street = request.address.street,
                building = request.address.building
            ).mapLeft { it.toError() },

            customerCartExtractor.getCart(forCustomer = CustomerId(request.customerId))
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
            customerOrderPersister.save(order)
            order.id
        }
    }
}

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