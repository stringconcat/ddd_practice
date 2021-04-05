package com.stringconcat.ddd.delivery.domain

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.stringconcat.ddd.common.types.base.AggregateRoot
import com.stringconcat.ddd.common.types.base.ValueObject
import com.stringconcat.ddd.common.types.base.Version
import com.stringconcat.ddd.common.types.common.Address
import com.stringconcat.ddd.common.types.common.Count
import com.stringconcat.ddd.common.types.error.BusinessError

data class DeliveryOrderId(val value: Long)

class DeliveryOrder internal constructor(
    id: DeliveryOrderId,
    version: Version,
    val deliveryAddress: Address,
    val orderItems: List<OrderItem>
) : AggregateRoot<DeliveryOrderId>(id, version) {

    companion object {
        fun create(
            id: DeliveryOrderId,
            deliveryAddress: Address,
            orderItems: List<OrderItem>
        ): Either<OrderWithNoItems, DeliveryOrder> {
            return if (orderItems.isNotEmpty()) {
                DeliveryOrder(
                    id = id,
                    version = Version.new(),
                    deliveryAddress = deliveryAddress,
                    orderItems = orderItems
                ).apply {
                    addEvent(DeliveryOrderCreatedDomainEvent(id))
                }.right()
            } else {
                return OrderWithNoItems.left()
            }
        }
    }
}

data class OrderItem(
    val meal: Meal,
    val count: Count
) : ValueObject

object OrderWithNoItems : BusinessError
