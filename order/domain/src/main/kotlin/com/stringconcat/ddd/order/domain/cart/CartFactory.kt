package com.stringconcat.ddd.order.domain.cart

import com.stringconcat.ddd.common.types.base.Version
import java.time.OffsetDateTime

class CartFactory(
    private val idGenerator: CartIdGenerator,
    private val cartExtractor: CartExtractor
) {

    fun createOrGetCart(forCustomer: CustomerId): Cart =
        cartExtractor.getCart(forCustomer)
            ?: Cart(
                id = idGenerator.generate(),
                customerId = forCustomer,
                created = OffsetDateTime.now(),
                version = Version.generate(),
                meals = emptyMap()
            ).apply {
                addCartEvent(CartHasBeenCreatedEvent(cartId = this.id))
            }
}