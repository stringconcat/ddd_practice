package com.stringconcat.ddd.shop.usecase.order.scenarios

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.rightIfNotNull
import com.stringconcat.ddd.shop.domain.order.CheckoutError
import com.stringconcat.ddd.shop.domain.order.ShopOrder
import com.stringconcat.ddd.shop.domain.order.ShopOrderIdGenerator
import com.stringconcat.ddd.shop.domain.order.MealPriceProvider
import com.stringconcat.ddd.shop.domain.order.CustomerHasActiveOrder
import com.stringconcat.ddd.shop.usecase.cart.access.CartExtractor
import com.stringconcat.ddd.shop.usecase.order.Checkout
import com.stringconcat.ddd.shop.usecase.order.CheckoutRequest
import com.stringconcat.ddd.shop.usecase.order.CheckoutUseCaseError
import com.stringconcat.ddd.shop.usecase.order.PaymentInfo
import com.stringconcat.ddd.shop.usecase.order.access.ShopOrderPersister
import com.stringconcat.ddd.shop.usecase.order.providers.PaymentUrlProvider

class CheckoutUseCase(
    private val idGenerator: ShopOrderIdGenerator,
    private val cartExtractor: CartExtractor,
    private val activeOrder: CustomerHasActiveOrder,
    private val priceProvider: MealPriceProvider,
    private val paymentUrlProvider: PaymentUrlProvider,
    private val shopOrderPersister: ShopOrderPersister
) : Checkout {

    override fun execute(request: CheckoutRequest): Either<CheckoutUseCaseError, PaymentInfo> =

        cartExtractor
            .getCart(forCustomer = request.forCustomer)
            .rightIfNotNull { CheckoutUseCaseError.CartNotFound }
            .flatMap { cart ->
                ShopOrder.checkout(
                    idGenerator = idGenerator,
                    customerHasActiveOrder = activeOrder,
                    priceProvider = priceProvider,
                    address = request.deliveryTo,
                    cart = cart
                ).mapLeft { err -> err.toError() }
            }.map { order ->
                shopOrderPersister.save(order)
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