package com.stringconcat.ddd.mock

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class CrmController {

    var result = Result.SUCCESS

    @PostMapping("/orders")
    fun importOrder(): Response {
        return Response(result)
    }
}

data class Response(val result: Result)
enum class Result { SUCCESS, ALREADY_EXISTS }