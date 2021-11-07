package com.stringconcat.ddd.shop.telnet

import arrow.core.Either
import arrow.core.left
import arrow.core.right

object LongParameterExtractor {

    fun extract(line: String): Either<String, Long> {
        val split = line.split(" ")
        return if (split.size != 2 || split[1].toLongOrNull() == null) {
            "Invalid argument".left()
        } else {
            split[1].toLong().right()
        }
    }
}