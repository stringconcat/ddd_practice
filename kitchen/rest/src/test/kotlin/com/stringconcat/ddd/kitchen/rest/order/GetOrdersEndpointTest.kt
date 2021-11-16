package com.stringconcat.ddd.kitchen.rest.order

import APPLICATION_HAL_JSON
import MockGetOrders
import apiV1Url
import com.stringconcat.ddd.kitchen.usecase.order.GetOrders
import orderDetails
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@WebMvcTest
internal class GetOrdersEndpointTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var getOrders: MockGetOrders

    @Test
    fun `returned successfully`() {

        val single = orderDetails()
        val firstItem = single.meals[0]

        getOrders.response = listOf(single)

        mockMvc.get("/rest/kitchen/v1/orders")
            .andExpect {
                status { isOk() }
                content {
                    contentType(APPLICATION_HAL_JSON)
                    jsonPath("$._embedded.orders[0].id") { value(single.id.value) }
                    jsonPath("$._embedded.orders[0].cooked") { value(single.cooked) }
                    jsonPath("$._embedded.orders[0].meals.length()") { value(single.meals.size) }
                    jsonPath("$._embedded.orders[0].meals[0].meal") { value(firstItem.meal.value) }
                    jsonPath("$._embedded.orders[0].meals[0].count") { value(firstItem.count.value) }
                    jsonPath("$._embedded.orders[0]._links.self.href") {
                        value(apiV1Url("/orders/${single.id.value}"))
                    }
                }
            }
    }

    @Configuration
    class TestConfiguration {

        @Bean
        fun mockGetOrders() = MockGetOrders()

        @Bean
        fun cancelOrderEndpoint(getOrders: GetOrders) = GetOrdersEndpoint(getOrders)
    }
}