package com.stringconcat.ddd.shop.rest.menu

import MockRemoveMealFromMenu
import arrow.core.left
import arrow.core.right
import com.stringconcat.ddd.shop.domain.mealId
import com.stringconcat.ddd.shop.rest.API_V1_MENU_DELETE_BY_ID
import com.stringconcat.ddd.shop.usecase.menu.RemoveMealFromMenu
import com.stringconcat.ddd.shop.usecase.menu.RemoveMealFromMenuUseCaseError
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
import org.springframework.test.web.servlet.delete
import withHost
import withId

@WebMvcTest
@ContextConfiguration(classes = [RemoveMealFromMenuEndpointTest.TestConfiguration::class])
internal class RemoveMealFromMenuEndpointTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var removeMealFromMenu: MockRemoveMealFromMenu

    @Test
    fun `meal not found`() {
        removeMealFromMenu.response = RemoveMealFromMenuUseCaseError.MealNotFound.left()

        val url = API_V1_MENU_DELETE_BY_ID.withId(mealId().toLongValue()).withHost()
        mockMvc.delete(url)
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
    fun `removed successfully`() {
        removeMealFromMenu.response = Unit.right()

        val url = API_V1_MENU_DELETE_BY_ID.withId(mealId().toLongValue()).withHost()
        mockMvc.delete(url)
            .andExpect {
                content {
                    status { isNoContent() }
                    content {
                        string("")
                    }
                }
            }
    }

    @Configuration
    class TestConfiguration {

        @Bean
        fun removeMealFromMenu() = MockRemoveMealFromMenu()

        @Bean
        fun removeMealFromMenuEndpoint(removeMealFromMenu: RemoveMealFromMenu) =
            RemoveMealFromMenuEndpoint(removeMealFromMenu)
    }
}