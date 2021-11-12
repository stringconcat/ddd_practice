package com.stringconcat.ddd.shop.rest

import arrow.core.Nel
import org.springframework.hateoas.mediatype.problem.Problem
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI

typealias Message = String

data class ValidationError(val message: Message)

val baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString()

fun restBusinessError(title: String, code: String) =
    ResponseEntity
        .unprocessableEntity()
        .contentType(MediaType.APPLICATION_PROBLEM_JSON)
        .body(
            Problem.create().withStatus(HttpStatus.UNPROCESSABLE_ENTITY)
                .withType(URI("$baseUrl/$code"))
                .withTitle(title)
        )

fun resourceNotFound() =
    ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .contentType(MediaType.APPLICATION_PROBLEM_JSON)
        .body(
            Problem.create().withStatus(HttpStatus.NOT_FOUND)
                .withType(URI("$baseUrl/resource_not_found"))
                .withTitle("Resource not found"))

fun Nel<ValidationError>.toInvalidParamsBadRequest() =
    ResponseEntity
        .badRequest()
        .contentType(MediaType.APPLICATION_PROBLEM_JSON)
        .body(Problem.create(mapOf("invalid_params" to this))
            .withStatus(HttpStatus.BAD_REQUEST)
            .withType(URI("$baseUrl/bad_request"))
            .withTitle("Bad request")
        )

fun created(location: URI) = ResponseEntity.created(location).build<Any>()

fun noContent() = ResponseEntity.noContent().build<Any>()