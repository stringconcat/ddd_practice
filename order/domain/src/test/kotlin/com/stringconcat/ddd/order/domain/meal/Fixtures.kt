package com.stringconcat.ddd.order.domain.meal

import arrow.core.Either
import com.stringconcat.ddd.common.types.base.Version
import com.stringconcat.ddd.common.types.common.Address
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

fun mealName(): MealName {
    val result = MealName.from("Name ${Random.nextInt()}")
    check(result is Either.Right<MealName>)
    return result.b
}

fun mealDescription(): MealDescription {
    val result = MealDescription.from("Description ${Random.nextInt()}")
    check(result is Either.Right<MealDescription>)
    return result.b
}

fun price(): Price {
    val result = Price.from(BigDecimal(Random.nextInt(1, 500000)))
    check(result is Either.Right<Price>)
    return result.b
}

fun version() = Version.generate()

fun mealId() = MealId(Random.nextLong())

fun meal(removed: Boolean = false): Meal {
    val mealId = mealId()
    val price = price()
    val name = mealName()
    val description = mealDescription()
    val address = address()
    val version = version()

    return MealRestorer.restoreMeal(
        id = mealId,
        name = name,
        removed = removed,
        description = description,
        address = address,
        price = price,
        version = version
    )
}