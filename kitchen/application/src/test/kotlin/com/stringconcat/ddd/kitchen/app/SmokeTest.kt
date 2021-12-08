package com.stringconcat.ddd.kitchen.app

import com.stringconcat.ddd.kitchen.app.configuration.ApplicationConfiguration
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest(classes = [ApplicationConfiguration::class])
@AutoConfigureMockMvc
internal class SmokeTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun `check context started`() {
        mockMvc.get("/")
            .andExpect {
                status { isOk() }
                content {
                    jsonPath("$._links") { exists() }
                }
            }
    }
}