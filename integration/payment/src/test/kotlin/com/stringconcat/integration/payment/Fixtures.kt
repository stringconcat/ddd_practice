package com.stringconcat.integration.payment

import arrow.core.Either
import com.stringconcat.ddd.shop.domain.menu.Price
import com.stringconcat.ddd.shop.domain.order.CustomerOrderId
import java.math.BigDecimal
import kotlin.random.Random

fun price(value: BigDecimal = BigDecimal(Random.nextInt(1, 500000))): Price {
    val result = Price.from(value)
    check(result is Either.Right<Price>)
    return result.b
}

fun orderId() = CustomerOrderId(Random.nextLong())