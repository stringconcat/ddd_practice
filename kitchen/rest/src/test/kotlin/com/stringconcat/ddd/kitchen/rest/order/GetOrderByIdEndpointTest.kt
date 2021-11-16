package com.stringconcat.ddd.kitchen.rest.order

import APPLICATION_HAL_JSON
import MockGetOrderById
import apiV1Url
import arrow.core.left
import arrow.core.right
import com.stringconcat.ddd.kitchen.domain.order.orderId
import com.stringconcat.ddd.kitchen.usecase.order.GetOrderById
import com.stringconcat.ddd.kitchen.usecase.order.GetOrderByIdUseCaseError
import io.kotest.matchers.collections.shouldHaveSize
import notFoundTypeUrl
import orderDetails
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@WebMvcTest
@ContextConfiguration(classes = [GetOrderByIdEndpointTest.TestConfiguration::class])
internal class GetOrderByIdEndpointTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var getOrderById: MockGetOrderById

    @Test
    fun `order not found`() {
        getOrderById.response = GetOrderByIdUseCaseError.OrderNotFound.left()
        val url = "/rest/kitchen/v1/orders/${orderId().value}"
        mockMvc.get(url)
            .andExpect {
                content {
                    contentType(MediaType.APPLICATION_PROBLEM_JSON)
                    status { isNotFound() }
                    content {
                        jsonPath("$.type") { notFoundTypeUrl() }
                        jsonPath("$.status") { value(HttpStatus.NOT_FOUND.value()) }
                    }
                }
            }
    }

    @Test
    fun `returned successfully`() {
        val details = orderDetails()
        details.meals.shouldHaveSize(1)
        val itemDetails = details.meals[0]

        getOrderById.response = details.right()
        val url = "/rest/kitchen/v1/orders/${details.id.value}"

        mockMvc.get(url)
            .andExpect {
                status { isOk() }
                content {
                    contentType(APPLICATION_HAL_JSON)
                    jsonPath("$.id") { value(details.id.value) }
                    jsonPath("$.cooked") { value(details.cooked) }
                    jsonPath("$.meals.length()") { value(1) }
                    jsonPath("$.meals[0].meal") { value(itemDetails.meal.value) }
                    jsonPath("$.meals[0].count") { value(itemDetails.count.value) }
                    jsonPath("$._links.self.href") {
                        value(apiV1Url("/orders/${details.id.value}"))
                    }
                }
            }
        getOrderById.verifyInvoked(details.id)
    }

    @Configuration
    class TestConfiguration {

        @Bean
        fun getOrderByIdEndpoint(getOrderById: GetOrderById) = GetOrderByIdEndpoint(getOrderById)

        @Bean
        fun getOrderById() = MockGetOrderById()
    }
}