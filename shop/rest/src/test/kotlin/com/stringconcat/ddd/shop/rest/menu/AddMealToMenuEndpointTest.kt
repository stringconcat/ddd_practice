package com.stringconcat.ddd.shop.rest.menu

import MockAddMealToMenu
import com.fasterxml.jackson.databind.ObjectMapper
import com.stringconcat.ddd.shop.usecase.menu.AddMealToMenu
import java.math.BigDecimal
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@WebMvcTest
@ContextConfiguration(classes = [AddMealToMenuEndpointTest.TestConfiguration::class])
internal class AddMealToMenuEndpointTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    private val mapper = ObjectMapper()

    @Test
    fun `validation error`() {
         mockMvc
            .post("/rest/v1/menu") {
                contentType = MediaType.APPLICATION_JSON
                content =
                    mapper.writeValueAsString(
                        AddMealToMenuRestRequest(name = "",
                            description = "",
                            price = BigDecimal.ONE.setScale(20))
                    )
            }.andExpect {
                content {
                    contentType(MediaType.APPLICATION_PROBLEM_JSON)
                    status { isBadRequest() }
                }
             }
            .andReturn().response.contentAsString
    }

    @Configuration
    class TestConfiguration {

        @Bean
        fun addMealToMenuEndpoint(addMealToMenu: AddMealToMenu) = AddMealToMenuEndpoint(addMealToMenu)

        @Bean
        fun addMealToMenu() = MockAddMealToMenu()
    }
}