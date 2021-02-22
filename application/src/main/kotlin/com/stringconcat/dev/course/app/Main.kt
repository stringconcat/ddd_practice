package com.stringconcat.dev.course.app

import com.stringconcat.dev.course.app.configuration.ApplicationConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
@Import(ApplicationConfiguration::class)
class DDDApplication

fun main(args: Array<String>) {
    runApplication<DDDApplication>(*args)
}

@RestController
class MainController {

    @GetMapping(path = ["/"])
    fun helloThere(): String {
        @Suppress("FunctionOnlyReturningConstant")
        return """{"message": "HelloThere"}"""
    }
}
