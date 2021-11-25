package com.stringconcat.dev.course.app.controllers

import com.stringconcat.ddd.shop.persistence.postgresql.StorageConflictException
import java.net.URI
import java.util.UUID
import org.slf4j.LoggerFactory
import org.springframework.hateoas.mediatype.problem.Problem
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@ControllerAdvice
class GlobalErrorHandler {

    private val logger = LoggerFactory.getLogger(GlobalErrorHandler::class.java)

    @ExceptionHandler(value = [Throwable::class])
    fun handleThrowable(ex: Throwable) =
        logErrorAndBuildResponse(
            throwable = ex,
            status = HttpStatus.INTERNAL_SERVER_ERROR,
            title = "Internal error",
            code = "internal_error")

    @ExceptionHandler(value = [StorageConflictException::class])
    fun handleStorageConflict(ex: StorageConflictException) =
        logErrorAndBuildResponse(
            throwable = ex,
            status = HttpStatus.CONFLICT,
            title = "Conflict",
            code = "conflict"
        )

    private fun logErrorAndBuildResponse(
        throwable: Throwable,
        status: HttpStatus,
        title: String,
        code: String,
    ): ResponseEntity<Problem> {
        val baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString()
        val errorId = UUID.randomUUID()
        logger.error("Unexpected exception [$errorId]", throwable)

        return ResponseEntity
            .status(status)
            .contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .body(
                Problem.create()
                    .withStatus(status)
                    .withType(URI("$baseUrl/$code"))
                    .withTitle("$title. Please contact your system administrator with error ID")
                    .withProperties(mapOf("errorId" to errorId))
            )
    }
}
