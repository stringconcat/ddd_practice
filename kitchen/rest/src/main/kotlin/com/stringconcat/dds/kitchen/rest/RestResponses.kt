package com.stringconcat.dds.kitchen.rest

import org.springframework.hateoas.mediatype.problem.Problem
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI

val baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString()

fun noContent() = ResponseEntity.noContent().build<Any>()

fun resourceNotFound() =
    ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .contentType(MediaType.APPLICATION_PROBLEM_JSON)
        .body(
            Problem.create().withStatus(HttpStatus.NOT_FOUND)
                .withType(URI("$baseUrl/resource_not_found"))
                .withTitle("Resource not found")
        )