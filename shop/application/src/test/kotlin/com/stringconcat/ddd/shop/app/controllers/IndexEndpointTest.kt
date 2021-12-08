package com.stringconcat.ddd.shop.app.controllers

import com.stringconcat.ddd.shop.app.toServerUrl
import com.stringconcat.ddd.shop.rest.API_V1_MENU_GET_ALL
import com.stringconcat.ddd.shop.rest.API_V1_ORDER_GET_ALL
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@WebMvcTest
@ContextConfiguration(classes = [IndexEndpointTest.TestConfiguration::class])
internal class IndexEndpointTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun `get index information`() {
        val url = "http://localhost"
        mockMvc.get(url)
            .andExpect {
                status { isOk() }
                content {
                    contentType("application/hal+json")
                    jsonPath("$._links.self.href") { value(url) }
                    jsonPath("$._links.menu.href") { value(API_V1_MENU_GET_ALL.toServerUrl()) }
                    jsonPath("$._links.orders.href") {
                        value("$API_V1_ORDER_GET_ALL?startId=0&limit=10".toServerUrl())
                    }
                }
            }
    }

    @Configuration
    class TestConfiguration {
        @Bean
        fun indexEndpoint() = IndexEndpoint()
    }
}