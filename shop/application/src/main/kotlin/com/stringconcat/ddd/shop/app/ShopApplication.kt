package com.stringconcat.ddd.shop.app

import com.stringconcat.ddd.shop.app.configuration.ApplicationConfiguration
import org.springframework.boot.runApplication

fun main(args: Array<String>) {
    runApplication<ApplicationConfiguration>(*args)
}