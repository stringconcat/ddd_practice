package com.stringconcat.ddd.kitchen.domain.order

import com.stringconcat.ddd.common.types.base.AggregateRoot
import com.stringconcat.ddd.common.types.base.ValueObject
import com.stringconcat.ddd.common.types.base.Version
import com.stringconcat.ddd.common.types.common.Count

data class KitchenOrderId(val value: Long)

class KitchenOrder internal constructor(
    id: KitchenOrderId,
    val orderItems: Set<OrderItem>,
    version: Version
) : AggregateRoot<KitchenOrderId>(id, version) {

    var cooked = false
        internal set

    fun cooked() {
        if (!cooked) {
            cooked = true
            addEvent(KitchenOrderHasBeenCookedEvent(id))
        }
    }

    companion object {
        fun create(id: KitchenOrderId, orderItems: Set<OrderItem>): KitchenOrder {
            return KitchenOrder(
                id = id,
                orderItems = orderItems,
                version = Version.generate()
            ).apply {
                addEvent(KitchenOrderHasBeenCreatedEvent(id))
            }
        }
    }
}

data class OrderItem(val meal: Meal, val count: Count) : ValueObject {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OrderItem

        if (meal != other.meal) return false

        return true
    }

    override fun hashCode(): Int {
        return meal.hashCode()
    }
}