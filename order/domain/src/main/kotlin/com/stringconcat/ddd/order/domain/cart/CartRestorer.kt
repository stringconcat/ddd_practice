package com.stringconcat.ddd.order.domain.cart

import com.stringconcat.ddd.common.types.base.Version
import com.stringconcat.ddd.common.types.common.Count
import com.stringconcat.ddd.order.domain.menu.MealId
import java.time.OffsetDateTime

object CartRestorer {

    fun restoreCart(
        id: CartId,
        customerId: CustomerId,
        created: OffsetDateTime,
        meals: Map<MealId, Count>,
        version: Version
    ): Cart {
        return Cart(
            id = id,
            customerId = customerId,
            created = created,
            meals = meals,
            version = version
        )
    }
}