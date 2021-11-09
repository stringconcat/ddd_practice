package com.stringconcat.ddd.shop.rest.menu

import arrow.core.Nel
import java.net.URI
import org.springframework.hateoas.mediatype.problem.Problem
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
typealias Message = String

data class ValidationError(val message: Message)

fun validationError(errors: Nel<ValidationError>) =
    ResponseEntity
        .badRequest()
        .contentType(MediaType.APPLICATION_PROBLEM_JSON)
        .body(Problem.create(mapOf("invalid_params" to errors))
            .withStatus(HttpStatus.BAD_REQUEST)
            .withType(URI("http://restaurant.stringconcat.com/bad_request"))
            .withTitle("Bad request")
        )