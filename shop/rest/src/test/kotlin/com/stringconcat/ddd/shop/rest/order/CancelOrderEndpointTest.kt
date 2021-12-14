package com.stringconcat.ddd.shop.rest.order

import MockCancelOrder
import arrow.core.left
import arrow.core.right
import com.stringconcat.ddd.shop.domain.orderId
import com.stringconcat.ddd.shop.rest.API_V1_ORDER_CANCEL_BY_ID
import com.stringconcat.ddd.shop.usecase.order.CancelOrder
import com.stringconcat.ddd.shop.usecase.order.CancelOrderUseCaseError
import errorTypeUrl
import notFoundTypeUrl
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.hateoas.config.EnableHypermediaSupport
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.put
import withHost
import withId

@WebMvcTest
@ContextConfiguration(classes = [CancelOrderEndpointTest.TestConfiguration::class])
internal class CancelOrderEndpointTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var mockCancelOrder: MockCancelOrder

    @Test
    fun `order not found`() {
        val orderId = orderId()
        mockCancelOrder.response = CancelOrderUseCaseError.OrderNotFound.left()

        mockMvc.put(API_V1_ORDER_CANCEL_BY_ID.withId(orderId.toLongValue()).withHost())
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

        mockCancelOrder.verifyInvoked(orderId)
    }

    @Test
    fun `invalid order state`() {
        val orderId = orderId()
        mockCancelOrder.response = CancelOrderUseCaseError.InvalidOrderState.left()

        mockMvc.put(API_V1_ORDER_CANCEL_BY_ID.withId(orderId.toLongValue()).withHost())
            .andExpect {
                content {
                    contentType(MediaType.APPLICATION_PROBLEM_JSON)
                    status { isUnprocessableEntity() }
                    content {
                        jsonPath("$.type") { value(errorTypeUrl("invalid_state")) }
                        jsonPath("$.status") { value(422) }
                    }
                }
            }

        mockCancelOrder.verifyInvoked(orderId)
    }

    @Test
    fun `successfully canceled`() {

        val orderId = orderId()
        mockCancelOrder.response = Unit.right()

        mockMvc.put(API_V1_ORDER_CANCEL_BY_ID.withId(orderId.toLongValue()).withHost())
            .andExpect {
                content {
                    status { isNoContent() }
                }
            }

        mockCancelOrder.verifyInvoked(orderId)
    }

    @Configuration
    @EnableHypermediaSupport(type = [EnableHypermediaSupport.HypermediaType.HAL_FORMS])
    class TestConfiguration {

        @Bean
        fun mockCancelOrder() = MockCancelOrder()

        @Bean
        fun cancelOrderEndpoint(cancelOrder: CancelOrder) = CancelOrderEndpoint(cancelOrder)
    }
}