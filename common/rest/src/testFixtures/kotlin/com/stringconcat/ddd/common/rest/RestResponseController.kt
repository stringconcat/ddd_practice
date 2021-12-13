package com.stringconcat.ddd.common.rest

import arrow.core.nonEmptyListOf
import java.net.URI
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

const val BUSINESS_ERROR_URL = "/businessError"
const val NOT_FOUND_URL = "/notFound"
const val CREATED_URL = "/created"
const val NO_CONTENT_URL = "/noContent"
const val INVALID_PARAMS_URL = "/bad_request"

@RestController
class RestResponseController {

    @GetMapping(BUSINESS_ERROR_URL)
    fun businessErrorResponse() = restBusinessError("Error", "error_code")

    @GetMapping(NOT_FOUND_URL)
    fun notFoundResponse() = resourceNotFound()

    @GetMapping(CREATED_URL)
    fun createdResponse() = created(URI("http://location"))

    @GetMapping(NO_CONTENT_URL)
    fun noContentResponse() = noContent()

    @GetMapping(INVALID_PARAMS_URL)
    fun validationErrorResponse() =
        nonEmptyListOf(
            ValidationError("field1 is empty"),
            ValidationError("field2 is too long")
        ).toInvalidParamsBadRequest()
}