package com.stringconcat.ddd.order.domain

import arrow.core.Either
import com.stringconcat.ddd.common.types.base.Version
import com.stringconcat.ddd.common.types.common.Address
import com.stringconcat.ddd.common.types.common.Count
import com.stringconcat.ddd.order.domain.cart.Cart
import com.stringconcat.ddd.order.domain.cart.CartId
import com.stringconcat.ddd.order.domain.cart.CartRestorer
import com.stringconcat.ddd.order.domain.cart.CustomerId
import com.stringconcat.ddd.order.domain.menu.MealDescription
import com.stringconcat.ddd.order.domain.menu.MealId
import com.stringconcat.ddd.order.domain.menu.MealName
import com.stringconcat.ddd.order.domain.menu.Price
import com.stringconcat.ddd.order.domain.menu.MealRestorer
import com.stringconcat.ddd.order.domain.menu.Meal
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.UUID
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

fun guestId() = CustomerId(UUID.randomUUID().toString())

fun cartId() = CartId(Random.nextLong())


fun count(value: Int = Random.nextInt(20, 5000)): Count {
    val result = Count.from(value)
    check(result is Either.Right<Count>)
    return result.b
}

fun cart(meals: Map<MealId, Count> = emptyMap()): Cart {
    val cartId = cartId()
    val guestId = guestId()
    val version = version()
    val created = OffsetDateTime.now()
    return CartRestorer.restoreCart(
        id = cartId,
        customerId = guestId,
        created = created,
        meals = meals,
        version = version
    )
}