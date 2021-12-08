package com.stringconcat.ddd.common.rest

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class ErrorController {

    @GetMapping("illegalState")
    fun illegalState(): Nothing = throw IllegalStateException("OOoops!")

    @RequestMapping(method = [RequestMethod.POST, RequestMethod.PATCH], path = ["jsonDto"])
    fun jsonData(@RequestBody dto: JsonDto) = dto

    data class JsonDto(val field: String)
}