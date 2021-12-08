package com.stringconcat.ddd.common.rest

import java.net.URI
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class RestResponseController {

    @GetMapping("businessError")
    fun businessErrorResponse() = restBusinessError("Error", "error_code")

    @GetMapping("notFound")
    fun notFoundResponse() = resourceNotFound()

    @GetMapping("created")
    fun createdResponse() = created(URI("http://location"))

    @GetMapping("noContent")
    fun noContentResponse() = noContent()
}