package com.stringconcat.ddd.shop.rest.menu

import MockAddMealToMenu
import arrow.core.left
import arrow.core.right
import com.fasterxml.jackson.databind.ObjectMapper
import com.stringconcat.ddd.shop.domain.mealDescription
import com.stringconcat.ddd.shop.domain.mealId
import com.stringconcat.ddd.shop.domain.mealName
import com.stringconcat.ddd.shop.domain.price
import com.stringconcat.ddd.shop.usecase.menu.AddMealToMenu
import com.stringconcat.ddd.shop.usecase.menu.AddMealToMenuUseCaseError
import endpointApiV1Url
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
import typeApiV1Url

@WebMvcTest
@ContextConfiguration(classes = [AddMealToMenuEndpointTest.TestConfiguration::class])
internal class AddMealToMenuEndpointTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var mockAddMealToMenu: MockAddMealToMenu

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
                    content {
                        jsonPath("$.type") { value(typeApiV1Url("bad_request")) }
                        jsonPath("$.status") { value(400) }
                        jsonPath("$.invalid_params.length()") { value(3) }
                    }
                }
            }
    }

    @Test
    fun `meal already exists`() {
        mockAddMealToMenu.response = AddMealToMenuUseCaseError.AlreadyExists.left()

        val name = mealName()
        val description = mealDescription()
        val price = price()

        mockMvc
            .post("/rest/v1/menu") {
                contentType = MediaType.APPLICATION_JSON
                content =
                    mapper.writeValueAsString(
                        AddMealToMenuRestRequest(name = name.value,
                            description = description.value,
                            price = price.value)
                    )
            }.andExpect {
                content {
                    contentType(MediaType.APPLICATION_PROBLEM_JSON)
                    status { isUnprocessableEntity() }
                    content {
                        jsonPath("$.type") { value(typeApiV1Url("already_exists")) }
                        jsonPath("$.status") { value(422) }
                    }
                }
            }

        mockAddMealToMenu.verifyInvoked(name, description, price)
    }

    @Test
    fun `created successfully`() {
        mockAddMealToMenu.response = mealId().right()

        val name = mealName()
        val description = mealDescription()
        val price = price()

        mockMvc
            .post("/rest/v1/menu") {
                contentType = MediaType.APPLICATION_JSON
                content =
                    mapper.writeValueAsString(
                        AddMealToMenuRestRequest(name = name.value,
                            description = description.value,
                            price = price.value)
                    )
            }.andExpect {
                content {
                    status { isCreated() }
                    content {
                        string("")
                    }
                    header { string("Location", endpointApiV1Url("/menu")) }
                }
            }
        mockAddMealToMenu.verifyInvoked(name, description, price)
    }

    @Configuration
    class TestConfiguration {

        @Bean
        fun addMealToMenuEndpoint(addMealToMenu: AddMealToMenu) = AddMealToMenuEndpoint(addMealToMenu)

        @Bean
        fun addMealToMenu() = MockAddMealToMenu()
    }
}