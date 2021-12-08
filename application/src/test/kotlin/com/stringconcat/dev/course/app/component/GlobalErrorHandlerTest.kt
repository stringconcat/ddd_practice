package com.stringconcat.dev.course.app.component

import com.stringconcat.dev.course.app.ShopComponentTestConfiguration
import com.stringconcat.dev.course.app.TEST_TELNET_PORT
import com.stringconcat.dev.course.app.UUID_PATTERN
import com.stringconcat.dev.course.app.toServerUrl
import org.hamcrest.core.StringContains.containsString
import org.hamcrest.core.StringRegularExpression.matchesRegex
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put

@SpringBootTest(classes = [ShopComponentTestConfiguration::class],
    properties = ["telnet.port=$TEST_TELNET_PORT"])
@AutoConfigureMockMvc
class GlobalErrorHandlerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `illegal state exception must be converted to HTTP 500`() {
        mockMvc.get("/illegalState")
            .andExpect {
                status { isInternalServerError() }
                content {
                    contentType(MediaType.APPLICATION_PROBLEM_JSON)
                    jsonPath("$.type") { value("/internal_error".toServerUrl()) }
                    jsonPath("$.status") { value(HttpStatus.INTERNAL_SERVER_ERROR.value()) }
                    jsonPath("$.errorId") { value(matchesRegex(UUID_PATTERN)) }
                    jsonPath("$.title") { value(containsString("Internal error")) }
                }
            }
    }

    @Test
    fun `storage conflict state exception must be converted to HTTP 409`() {
        mockMvc.get("/storageConflict")
            .andExpect {
                status { isConflict() }
                content {
                    contentType(MediaType.APPLICATION_PROBLEM_JSON)
                    jsonPath("$.type") { value("/conflict".toServerUrl()) }
                    jsonPath("$.status") { value(HttpStatus.CONFLICT.value()) }
                    jsonPath("$.errorId") { value(matchesRegex(UUID_PATTERN)) }
                    jsonPath("$.title") { value(containsString("Conflict")) }
                }
            }
    }

    @Test
    fun `url not found must be converted to HTTP 404 with description`() {
        mockMvc
            .get("/someNotExistingUrl")
            .andExpect {
                status { isNotFound() }
                content {
                    contentType(MediaType.APPLICATION_PROBLEM_JSON)
                    jsonPath("$.type") { value("/path_not_found".toServerUrl()) }
                    jsonPath("$.status") { value(HttpStatus.NOT_FOUND.value()) }
                    jsonPath("$.errorId") { doesNotExist() }
                    jsonPath("$.title") { value("Path not found") }
                }
            }
    }

    @Test
    fun `request method not supported must be converted to HTTP 405`() {
        mockMvc
            .put("/jsonDto") {
                contentType = MediaType.APPLICATION_JSON
            }
            .andExpect {
                status { isMethodNotAllowed() }
                content {
                    contentType(MediaType.APPLICATION_PROBLEM_JSON)
                    jsonPath("$.type") { value("/method_not_allowed".toServerUrl()) }
                    jsonPath("$.status") { value(HttpStatus.METHOD_NOT_ALLOWED.value()) }
                    jsonPath("$.errorId") { doesNotExist() }
                    jsonPath("$.title") { value("Method not allowed") }
                }
                header {
                    string(HttpHeaders.ALLOW, "POST,PATCH")
                }
            }
    }

    @Test
    fun `content type not supported must be converted to HTTP 415`() {
        mockMvc
            .post("/jsonDto") {
                contentType = MediaType.TEXT_HTML
            }
            .andExpect {
                status { isUnsupportedMediaType() }
                content {
                    contentType(MediaType.APPLICATION_PROBLEM_JSON)
                    jsonPath("$.type") { value("/media_type_unsupported".toServerUrl()) }
                    jsonPath("$.status") { value(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value()) }
                    jsonPath("$.errorId") { doesNotExist() }
                    jsonPath("$.title") { value("Media type is unsupported") }
                }
                header {
                    string(HttpHeaders.ACCEPT, "application/json, application/*+json")
                }
            }
    }

    @Test
    fun `invalid json must be converted to HTTP 400 with description`() {
        mockMvc
            .post("/jsonDto") {
                contentType = MediaType.APPLICATION_JSON
                content = """{ "foo": "bar" }"""
            }
            .andExpect {
                status { isBadRequest() }
                content {
                    contentType(MediaType.APPLICATION_PROBLEM_JSON)
                    jsonPath("$.type") { value("/message_not_readable".toServerUrl()) }
                    jsonPath("$.status") { value(HttpStatus.BAD_REQUEST.value()) }
                    jsonPath("$.errorId") { doesNotExist() }
                    jsonPath("$.title") { value("Message isn't readable") }
                }
            }
    }
}