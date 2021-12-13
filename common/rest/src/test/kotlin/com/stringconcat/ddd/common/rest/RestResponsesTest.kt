package com.stringconcat.ddd.common.rest

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@SpringBootTest(classes = [RestResponsesTest.TestConfiguration::class])
@AutoConfigureMockMvc
internal class RestResponsesTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `build business error`() {
        mockMvc.get(BUSINESS_ERROR_URL)
            .andExpect {
                status { isUnprocessableEntity() }
                content {
                    contentType(MediaType.APPLICATION_PROBLEM_JSON)
                    status { isUnprocessableEntity() }
                    content {
                        jsonPath("$.status") { value(HttpStatus.UNPROCESSABLE_ENTITY.value()) }
                        jsonPath("$.title") { value(ERROR_TITLE) }
                        jsonPath("$.type") { value(BASE_URI.plus(ERROR_CODE_TYPE)) }
                    }
                }
            }
    }

    @Test
    fun `build not found error`() {
        mockMvc.get(NOT_FOUND_URL)
            .andExpect {
                status { HttpStatus.NOT_FOUND }
                content {
                    contentType(MediaType.APPLICATION_PROBLEM_JSON)
                    content {
                        jsonPath("$.status") { value(HttpStatus.NOT_FOUND.value()) }
                        jsonPath("$.title") { value(NOT_FOUND_TITLE) }
                        jsonPath("$.type") { value(BASE_URI.plus(NOT_FOUND_TYPE)) }
                    }
                }
            }
    }

    @Test
    fun `build created response`() {
        mockMvc.get(CREATED_URL)
            .andExpect {
                status { HttpStatus.CREATED }
                header {
                    string(HttpHeaders.LOCATION, LOCATION_URL)
                }
                jsonPath("$") { doesNotExist() }
            }
    }

    @Test
    fun `build no content response`() {
        mockMvc.get(NO_CONTENT_URL)
            .andExpect {
                status { HttpStatus.NO_CONTENT }
                jsonPath("$") { doesNotExist() }
            }
    }

    @Test
    fun `build invalid params error response`() {
        mockMvc.get(INVALID_PARAMS_URL)
            .andExpect {
                status { HttpStatus.BAD_REQUEST }
                content {
                    contentType(MediaType.APPLICATION_PROBLEM_JSON)
                    content {
                        jsonPath("$.status") { value(HttpStatus.BAD_REQUEST.value()) }
                        jsonPath("$.title") { value(INVALID_PARAMS_TITLE) }
                        jsonPath("$.type") { value(BASE_URI.plus(INVALID_PARAMS_TYPE)) }
                    }
                }
            }
    }

    @Configuration
    @EnableWebMvc
    class TestConfiguration {
        @Bean
        fun restResponseController() = RestResponseController()
    }
}