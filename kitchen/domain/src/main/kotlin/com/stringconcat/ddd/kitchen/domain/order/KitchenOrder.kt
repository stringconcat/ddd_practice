package com.stringconcat.ddd.kitchen.domain.order

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.stringconcat.ddd.common.types.base.AggregateRoot
import com.stringconcat.ddd.common.types.base.ValueObject
import com.stringconcat.ddd.common.types.base.Version
import com.stringconcat.ddd.common.types.common.Count
import com.stringconcat.ddd.common.types.error.BusinessError

data class KitchenOrderId(val value: Long)

class KitchenOrder internal constructor(
    id: KitchenOrderId,
    val orderItems: List<OrderItem>,
    version: Version
) : AggregateRoot<KitchenOrderId>(id, version) {

    var cooked = false
        internal set

    fun cook() {
        if (!cooked) {
            cooked = true
            addEvent(KitchenOrderCookedDomainEvent(id))
        }
    }

    companion object {
        fun create(
            id: KitchenOrderId,
            orderItems: List<OrderItem>
        ): Either<EmptyOrder, KitchenOrder> {
            return if (orderItems.isNotEmpty()) {

                KitchenOrder(
                    id = id,
                    orderItems = orderItems,
                    version = Version.new()
                ).apply {
                    addEvent(KitchenOrderCreatedDomainEvent(id))
                }.right()
            } else {
                EmptyOrder.left()
            }
        }
    }
}

object EmptyOrder : BusinessError

data class OrderItem(
    val meal: Meal,
    val count: Count
) : ValueObject