package com.stringconcat.ddd.kitchen.rest.order

import MockCookOrder
import arrow.core.left
import arrow.core.right
import com.stringconcat.ddd.kitchen.domain.order.orderId
import com.stringconcat.ddd.kitchen.usecase.order.CookOrder
import com.stringconcat.ddd.kitchen.usecase.order.CookOrderUseCaseError
import notFoundTypeUrl
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.put

@WebMvcTest
@ContextConfiguration(classes = [CookOrderEndpointTest.TestConfiguration::class])
internal class CookOrderEndpointTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var mockCookOrder: MockCookOrder

    @Test
    fun `order not found`() {
        val orderId = orderId()
        mockCookOrder.response = CookOrderUseCaseError.OrderNotFound.left()

        mockMvc.put("/rest/kitchen/v1/orders/${orderId.value}/cook")
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

        mockCookOrder.verifyInvoked(orderId)
    }

    @Test
    fun `successfully cooked`() {

        val orderId = orderId()
        mockCookOrder.response = Unit.right()

        mockMvc.put("/rest/kitchen/v1/orders/${orderId.value}/cook")
            .andExpect {
                content {
                    status { isNoContent() }
                }
            }

        mockCookOrder.verifyInvoked(orderId)
    }

    @Configuration
    class TestConfiguration {

        @Bean
        fun mockCookOrder() = MockCookOrder()

        @Bean
        fun cancelOrderEndpoint(cookOrder: CookOrder) = CookOrderEndpoint(cookOrder)
    }
}