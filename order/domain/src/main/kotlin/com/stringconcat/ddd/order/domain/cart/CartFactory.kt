package com.stringconcat.ddd.order.domain.cart

import com.stringconcat.ddd.common.types.base.Version
import java.time.OffsetDateTime

class CartFactory(
    private val idGenerator: CartIdGenerator,
    private val customerCartExtractor: CustomerCartExtractor
) {

    fun createOrGetCart(customerId: CustomerId): Cart {

        return customerCartExtractor.getCartByCustomerId(customerId)
            ?: Cart(
                id = idGenerator.generate(),
                customerId = customerId,
                created = OffsetDateTime.now(),
                version = Version.generate(),
                meals = emptyMap()
            ).apply {
                addCartEvent(CartHasBeenCreatedEvent(cartId = this.id))
            }
    }
}