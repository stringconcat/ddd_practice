package com.stringconcat.dev.course.app.controllers

import java.net.URI
import java.util.UUID
import org.slf4j.LoggerFactory
import org.springframework.hateoas.mediatype.problem.Problem
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.NoHandlerFoundException
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

//    @ExceptionHandler(value = [StorageConflictException::class])
//    fun handleStorageConflict(ex: StorageConflictException) =
//        logErrorAndBuildResponse(
//            throwable = ex,
//            status = HttpStatus.CONFLICT,
//            title = "Conflict",
//            code = "conflict"
//        )

    @ExceptionHandler(value = [NoHandlerFoundException::class])
    fun handleNotFound() =
        buildError(status = HttpStatus.NOT_FOUND,
            title = "Path not found",
            code = "path_not_found")

    @ExceptionHandler(value = [HttpRequestMethodNotSupportedException::class])
    fun handleMethodNotSupported(ex: HttpRequestMethodNotSupportedException): ResponseEntity<Problem> {

        val methods = ex.supportedHttpMethods

        val headers = HttpHeaders()
        if (methods != null) {
            headers.allow = methods
        }

        return buildError(status = HttpStatus.METHOD_NOT_ALLOWED,
            title = "Method not allowed",
            code = "method_not_allowed",
            additionalHeaders = headers)
    }

    @ExceptionHandler(value = [HttpMediaTypeNotSupportedException::class])
    fun handleMediaTypeUnsupported(ex: HttpMediaTypeNotSupportedException): ResponseEntity<Problem> {

        val headers = HttpHeaders()
        headers.accept = ex.supportedMediaTypes

        return buildError(status = HttpStatus.UNSUPPORTED_MEDIA_TYPE,
            title = "Media type is unsupported",
            code = "media_type_unsupported",
            additionalHeaders = headers)
    }

    @ExceptionHandler(value = [HttpMessageNotReadableException::class])
    fun handleNotReadable() =
        buildError(status = HttpStatus.BAD_REQUEST,
            title = "Message isn't readable",
            code = "message_not_readable")

    private fun logErrorAndBuildResponse(
        throwable: Throwable,
        status: HttpStatus,
        title: String,
        code: String,
    ): ResponseEntity<Problem> {
        val errorId = UUID.randomUUID()
        logger.error("Unexpected exception [$errorId]", throwable)
        return buildError(
            status = status,
            title = title,
            code = code,
            errorId = errorId)
    }

    private fun buildError(
        status: HttpStatus,
        title: String,
        code: String,
        errorId: UUID? = null,
        additionalHeaders: HttpHeaders? = null,
    ): ResponseEntity<Problem> {
        val baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString()

        var problem = Problem.create()
            .withStatus(status)
            .withType(URI("$baseUrl/$code"))

        problem = if (errorId != null) {
            problem
                .withProperties(mapOf("errorId" to errorId))
                .withTitle("$title. Please contact your system administrator with error ID")
        } else {
            problem.withTitle(title)
        }

        return ResponseEntity
            .status(status)
            .headers(additionalHeaders)
            .contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .body(problem)
    }
}
