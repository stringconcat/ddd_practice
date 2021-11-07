package com.stringconcat.ddd.common.types

import arrow.core.Either
import com.github.javafaker.Faker
import com.stringconcat.ddd.common.types.common.Count

val faker = Faker()

fun count(value: Int = faker.number().numberBetween(20, 5000)): Count {
    val result = Count.from(value)
    check(result is Either.Right<Count>)
    return result.value
}