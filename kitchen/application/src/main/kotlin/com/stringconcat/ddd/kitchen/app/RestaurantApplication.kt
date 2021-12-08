package com.stringconcat.ddd.kitchen.app

import com.stringconcat.ddd.kitchen.app.configuration.ApplicationConfiguration
import org.springframework.boot.runApplication

fun main(args: Array<String>) {
    runApplication<ApplicationConfiguration>(*args)
}