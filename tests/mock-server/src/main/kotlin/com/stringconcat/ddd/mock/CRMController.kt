package com.stringconcat.ddd.mock

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class CRMController {

    @GetMapping
    @Suppress("FunctionOnlyReturningConstant")
    fun hello() = "Hello!"
}