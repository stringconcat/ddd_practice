package com.stringconcat.ddd.shop.usecase.order

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.rightIfNotNull
import com.stringconcat.ddd.shop.domain.order.CheckoutError
import com.stringconcat.ddd.shop.domain.order.CustomerOrder
import com.stringconcat.ddd.shop.domain.order.CustomerOrderIdGenerator
import com.stringconcat.ddd.shop.domain.order.MealPriceProvider
import com.stringconcat.ddd.shop.domain.order.CustomerHasActiveOrder
import com.stringconcat.ddd.shop.usecase.cart.CartExtractor

class CheckoutUseCase(
    private val idGenerator: CustomerOrderIdGenerator,
    private val cartExtractor: CartExtractor,
    private val activeOrder: CustomerHasActiveOrder,
    private val priceProvider: MealPriceProvider,
    private val paymentUrlProvider: PaymentUrlProvider,
    private val customerOrderPersister: CustomerOrderPersister
) : Checkout {

    override fun execute(request: CheckoutRequest): Either<CheckoutUseCaseError, PaymentInfo> =

        cartExtractor
            .getCart(forCustomer = request.forCustomer)
            .rightIfNotNull { CheckoutUseCaseError.CartNotFound }
            .flatMap { cart ->
                CustomerOrder.checkout(
                    idGenerator = idGenerator,
                    activeOrder = activeOrder,
                    priceProvider = priceProvider,
                    address = request.deliveryTo,
                    cart = cart
                ).mapLeft { err -> err.toError() }
            }.map { order ->
                customerOrderPersister.save(order)
                PaymentInfo(
                    orderId = order.id,
                    price = order.totalPrice(),
                    paymentURL = paymentUrlProvider.provideUrl(order.id, order.totalPrice())
                )
            }
}

fun CheckoutError.toError(): CheckoutUseCaseError {
    return when (this) {
        CheckoutError.AlreadyHasActiveOrder -> CheckoutUseCaseError.AlreadyHasActiveOrder
        CheckoutError.EmptyCart -> CheckoutUseCaseError.EmptyCart
    }
}