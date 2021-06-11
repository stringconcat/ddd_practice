package com.stringconcat.ddd.shop.query

import arrow.core.Either
import com.stringconcat.ddd.common.types.common.Address
import com.stringconcat.ddd.shop.domain.menu.Price
import com.stringconcat.ddd.shop.domain.order.OrderState
import com.stringconcat.ddd.shop.domain.order.ShopOrderId
import com.stringconcat.ddd.shop.query.order.ShopOrderInfo
import com.stringconcat.ddd.shop.query.order.ShopOrderQueryHandler
import java.math.BigDecimal
import kotlin.math.absoluteValue
import kotlin.random.Random

fun address(): Address {

    val result = Address.from(
        street = "${Random.nextInt()}th ave",
        building = Random.nextInt().absoluteValue
    )

    check(result is Either.Right<Address>)
    return result.b
}

fun price(value: BigDecimal = BigDecimal(Random.nextInt(1, 500000))): Price {
    val result = Price.from(value)
    check(result is Either.Right<Price>)
    return result.b
}

fun shopOrderInfo() = ShopOrderInfo(
    id = orderId(),
    state = OrderState.CANCELLED,
    address = address(),
    total = price()
)

fun orderId() = ShopOrderId(Random.nextLong())

class TestShopOrderHandlerStub(private val result: List<ShopOrderInfo>) : ShopOrderQueryHandler {
    override fun getAll() = result
}