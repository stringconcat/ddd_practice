package com.stringconcat.dev.course.app.telnet.order.menu

import arrow.core.Either

object LongParameterExtractor {

    fun extract(line: String): Either<String, Long> {
        val split = line.split(" ")
        return if (split.size != 2 || split[1].toLongOrNull() == null) {
            Either.left("Invalid argument")
        } else {
            Either.right(split[1].toLong())
        }
    }
}