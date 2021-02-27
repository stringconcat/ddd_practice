package com.stringconcat.dev.course.app

import arrow.core.Either
import com.stringconcat.ddd.common.types.common.Count
import com.stringconcat.ddd.order.domain.menu.MealDescription
import com.stringconcat.ddd.order.domain.menu.MealId
import com.stringconcat.ddd.order.domain.menu.MealName
import com.stringconcat.ddd.order.domain.menu.Price
import com.stringconcat.ddd.order.domain.order.CustomerOrderId
import java.math.BigDecimal
import kotlin.random.Random

fun mealName(name: String = "Name ${Random.nextInt()}"): MealName {
    val result = MealName.from(name)
    check(result is Either.Right<MealName>)
    return result.b
}

fun orderId() = CustomerOrderId(Random.nextLong())

fun mealDescription(description: String = "Description ${Random.nextInt()}"): MealDescription {
    val result = MealDescription.from(description)
    check(result is Either.Right<MealDescription>)
    return result.b
}

fun price(value: BigDecimal = BigDecimal(Random.nextInt(1, 500000))): Price {
    val result = Price.from(value)
    check(result is Either.Right<Price>)
    return result.b
}

fun mealId(id: Long = Random.nextLong()) = MealId(id)

fun count(value: Int = Random.nextInt(20, 5000)): Count {
    val result = Count.from(value)
    check(result is Either.Right<Count>)
    return result.b
}