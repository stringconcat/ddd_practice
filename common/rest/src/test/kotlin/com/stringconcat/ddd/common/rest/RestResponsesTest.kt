package com.stringconcat.ddd.common.rest

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
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
        mockMvc.get("/businessError")
            .andExpect {
                status { isUnprocessableEntity() }
                content {
                    contentType(MediaType.APPLICATION_PROBLEM_JSON)
                    status { isUnprocessableEntity() }
                    content {
                        jsonPath("$.status") { value(HttpStatus.UNPROCESSABLE_ENTITY.value()) }
                        // add more checks
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