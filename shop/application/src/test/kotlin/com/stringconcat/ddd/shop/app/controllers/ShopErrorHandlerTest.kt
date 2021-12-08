package com.stringconcat.ddd.shop.app.controllers

import com.stringconcat.ddd.shop.app.ShopComponentTestConfiguration
import com.stringconcat.ddd.shop.app.TEST_TELNET_PORT
import com.stringconcat.ddd.shop.app.UUID_PATTERN
import com.stringconcat.ddd.shop.app.toServerUrl
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

@SpringBootTest(classes = [ShopComponentTestConfiguration::class], properties = ["telnet.port=$TEST_TELNET_PORT"])
@AutoConfigureMockMvc
internal class ShopErrorHandlerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

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
    fun `url not found smoke test`() {
        mockMvc
            .get("/someNotExistingUrl")
            .andExpect {
                status { isNotFound() }
                content {
                    contentType(MediaType.APPLICATION_PROBLEM_JSON)
                    jsonPath("$.type") { exists() }
                }
            }
    }
}