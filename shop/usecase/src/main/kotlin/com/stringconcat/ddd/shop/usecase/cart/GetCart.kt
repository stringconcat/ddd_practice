package com.stringconcat.ddd.shop.usecase.cart

import arrow.core.Either
import com.stringconcat.ddd.common.types.common.Count
import com.stringconcat.ddd.shop.domain.cart.CustomerId
import com.stringconcat.ddd.shop.domain.menu.MealId
import com.stringconcat.ddd.shop.domain.menu.MealName

interface GetCart {
    fun execute(forCustomer: CustomerId): Either<GetCartUseCaseError, CartInfo>
}

data class CartInfo(val forCustomer: CustomerId, val items: List<CartItem>)
data class CartItem(val mealId: MealId, val mealName: MealName, val count: Count)

sealed class GetCartUseCaseError(val message: String) {
    object CartNotFound : GetCartUseCaseError("Cart not found")
}