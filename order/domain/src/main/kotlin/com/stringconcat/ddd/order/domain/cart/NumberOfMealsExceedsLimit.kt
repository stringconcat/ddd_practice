package com.stringconcat.ddd.order.domain.cart

interface NumberOfMealsExceedsLimit {
    fun check(cart: Cart): Boolean
}
