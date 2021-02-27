package com.stringconcat.ddd.order.usecase.cart

import arrow.core.Either
import com.stringconcat.ddd.common.types.common.Count
import com.stringconcat.ddd.order.domain.cart.CustomerId
import com.stringconcat.ddd.order.domain.menu.MealId
import com.stringconcat.ddd.order.domain.menu.MealName

interface GetCart {
    fun execute(forCustomer: CustomerId): Either<GetCartUseCaseError, CartInfo>
}

data class CartInfo(val customerId: CustomerId, val items: List<CartItem>)
data class CartItem(val mealId: MealId, val mealName: MealName, val count: Count)

sealed class GetCartUseCaseError(val message: String) {
    object CartNotFound : GetCartUseCaseError("Cart not found")
}