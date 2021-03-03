package com.stringconcat.dev.course.app

import com.stringconcat.dev.course.app.configuration.ApplicationConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import

@SpringBootApplication
@Import(ApplicationConfiguration::class)
class RestaurantApplication

fun main(args: Array<String>) {
    runApplication<RestaurantApplication>(*args)
}