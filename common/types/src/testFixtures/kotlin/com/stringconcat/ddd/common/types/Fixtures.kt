package com.stringconcat.ddd.common.types

import arrow.core.Either
import com.stringconcat.ddd.common.types.common.Count
import kotlin.random.Random

fun count(value: Int = Random.nextInt(20, 5000)): Count {
    val result = Count.from(value)
    check(result is Either.Right<Count>)
    return result.value
}