package com.stringconcat.ddd.order.usecase.cart

import arrow.core.Either

interface GetCart {
    fun execute(forCustomer: String): Either<GetCartUseCaseError, CartInfo>
}

data class CartInfo(val customerId: String, val items: List<CartItem>)
data class CartItem(val mealId: Long, val mealName: String, val count: Int)

sealed class GetCartUseCaseError(val message: String) {
    object CartNotFound : GetCartUseCaseError("Cart not found")
}