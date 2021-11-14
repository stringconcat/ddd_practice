package com.stringconcat.ddd.shop.rest.order

import APPLICATION_HAL_JSON
import MockGetOrders
import arrow.core.left
import arrow.core.right
import badRequestTypeUrl
import com.stringconcat.ddd.shop.domain.orderId
import com.stringconcat.ddd.shop.usecase.order.GetOrders
import com.stringconcat.ddd.shop.usecase.order.GetOrdersUseCaseError
import orderDetails
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@WebMvcTest
internal class GetOrdersEndpointTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var getOrders: MockGetOrders

    @Test
    fun `limit reached`() {
        val startId = orderId()
        val limit = 10
        getOrders.response = GetOrdersUseCaseError.LimitExceed(limit + 1).left()

        val url = "/rest/shop/v1/orders?startId=${startId.value}&limit=$limit"
        mockMvc.get(url)
            .andExpect {
                content {
                    contentType(MediaType.APPLICATION_PROBLEM_JSON)
                    status { isBadRequest() }
                    content {
                        jsonPath("$.type") { value(badRequestTypeUrl()) }
                        jsonPath("$.status") { value(HttpStatus.BAD_REQUEST.value()) }
                        jsonPath("$.invalid_params.length()") { value(1) }
                        jsonPath("$.invalid_params[0].message") { value("Max limit is 10") }
                    }
                }
            }

        getOrders.verifyInvoked(startId, limit + 1)
    }

    @Test
    fun `returned successfully`() {
        val limit = 1

        val first = orderDetails()
        val second = orderDetails()

        getOrders.response = listOf(first, second).right()

        val url = "/rest/shop/v1/orders?startId=${first.id.value}&limit=$limit"
        mockMvc.get(url)
            .andExpect {
                status { isOk() }
                content {
                    contentType(APPLICATION_HAL_JSON)
                    jsonPath("$.count") { value(limit) }
                    jsonPath("$._embedded.orders.length()") { value(1) }
                    jsonPath("$._embedded.orders[0].id") { value(first.id.value) }
                    jsonPath("$._embedded.orders[0].totalPrice") { value(first.total.value.toString()) }
                }
            }
        getOrders.verifyInvoked(first.id, limit + 1)
    }

    @Configuration
    class TestConfiguration {

        @Bean
        fun getOrdersEndpoint(getOrders: GetOrders) = GetOrdersEndpoint(getOrders)

        @Bean
        fun getOrders() = MockGetOrders()
    }
}