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
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

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
}