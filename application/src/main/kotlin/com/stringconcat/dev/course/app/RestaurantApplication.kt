package com.stringconcat.dev.course.app

import com.stringconcat.dev.course.app.configuration.ApplicationConfiguration
import org.springframework.boot.runApplication

fun main(args: Array<String>) {
    runApplication<ApplicationConfiguration>(*args)
}