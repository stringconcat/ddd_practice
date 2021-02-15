package com.stringconcat.ddd.order.domain.cart

import com.stringconcat.ddd.common.types.base.Version
import java.time.OffsetDateTime

class CartFactory(
    private val idGenerator: CartIdGenerator,
    private val cartByGuestId: GetCartByGuestId
) {

    fun createOrGetCartForGuest(customerId: CustomerId): Cart {

        return cartByGuestId.getCartByGuestId(customerId)
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