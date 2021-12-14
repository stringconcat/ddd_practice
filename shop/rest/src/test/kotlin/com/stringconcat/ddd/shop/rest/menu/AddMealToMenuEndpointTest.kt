package com.stringconcat.ddd.shop.rest.menu

import MockAddMealToMenu
import arrow.core.left
import arrow.core.right
import badRequestTypeUrl
import com.fasterxml.jackson.databind.ObjectMapper
import com.stringconcat.ddd.shop.domain.mealDescription
import com.stringconcat.ddd.shop.domain.mealId
import com.stringconcat.ddd.shop.domain.mealName
import com.stringconcat.ddd.shop.domain.price
import com.stringconcat.ddd.shop.rest.API_V1_MENU_ADD_TO_MENU
import com.stringconcat.ddd.shop.rest.API_V1_MENU_GET_BY_ID
import com.stringconcat.ddd.shop.usecase.menu.AddMealToMenu
import com.stringconcat.ddd.shop.usecase.menu.AddMealToMenuUseCaseError
import errorTypeUrl
import java.math.BigDecimal
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import withHost
import withId

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
            .post(API_V1_MENU_ADD_TO_MENU) {
                contentType = MediaType.APPLICATION_JSON
                content =
                    mapper.writeValueAsString(
                        AddMealToMenuRestRequest(
                            name = "",
                            description = "",
                            price = BigDecimal.ONE.setScale(20)
                        )
                    )
            }.andExpect {
                content {
                    contentType(MediaType.APPLICATION_PROBLEM_JSON)
                    status { isBadRequest() }
                    content {
                        jsonPath("$.type") { value(badRequestTypeUrl()) }
                        jsonPath("$.status") { value(HttpStatus.BAD_REQUEST.value()) }
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
            .post(API_V1_MENU_ADD_TO_MENU) {
                contentType = MediaType.APPLICATION_JSON
                content =
                    mapper.writeValueAsString(
                        AddMealToMenuRestRequest(
                            name = name.toStringValue(),
                            description = description.toStringValue(),
                            price = price.toBigDecimalValue()
                        )
                    )
            }.andExpect {
                content {
                    contentType(MediaType.APPLICATION_PROBLEM_JSON)
                    status { isUnprocessableEntity() }
                    content {
                        jsonPath("$.type") { value(errorTypeUrl("already_exists")) }
                        jsonPath("$.status") { value(HttpStatus.UNPROCESSABLE_ENTITY.value()) }
                    }
                }
            }

        mockAddMealToMenu.verifyInvoked(name, description, price)
    }

    @Test
    fun `created successfully`() {
        val mealId = mealId()
        mockAddMealToMenu.response = mealId.right()

        val name = mealName()
        val description = mealDescription()
        val price = price()

        val url = API_V1_MENU_ADD_TO_MENU

        mockMvc
            .post(url) {
                contentType = MediaType.APPLICATION_JSON
                content =
                    mapper.writeValueAsString(
                        AddMealToMenuRestRequest(
                            name = name.toStringValue(),
                            description = description.toStringValue(),
                            price = price.toBigDecimalValue()
                        )
                    )
            }.andExpect {
                content {
                    status { isCreated() }
                    content {
                        string("")
                    }
                    header { string("Location", API_V1_MENU_GET_BY_ID.withId(mealId.toLongValue()).withHost()) }
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