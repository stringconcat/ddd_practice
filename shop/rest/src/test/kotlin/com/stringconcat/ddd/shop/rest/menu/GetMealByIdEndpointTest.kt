package com.stringconcat.ddd.shop.rest.menu

import APPLICATION_HAL_JSON
import MockGetMealById
import arrow.core.left
import arrow.core.right
import com.stringconcat.ddd.shop.domain.mealId
import com.stringconcat.ddd.shop.rest.API_V1_MENU_GET_BY_ID
import com.stringconcat.ddd.shop.usecase.menu.GetMealById
import com.stringconcat.ddd.shop.usecase.menu.GetMealByIdUseCaseError
import mealInfo
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
import org.springframework.test.web.servlet.get
import withHost
import withId

@WebMvcTest
@ContextConfiguration(classes = [GetMealByIdEndpointTest.TestConfiguration::class])
class GetMealByIdEndpointTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var getMealById: MockGetMealById

    @Test
    fun `meal not found`() {
        getMealById.response = GetMealByIdUseCaseError.MealNotFound.left()

        val url = API_V1_MENU_GET_BY_ID.withId(mealId().value).withHost()
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
        val mealInfo = mealInfo()
        getMealById.response = mealInfo.right()

        val url = API_V1_MENU_GET_BY_ID.withId(mealInfo.id.value).withHost()
        mockMvc.get(url)
            .andExpect {
                status { isOk() }
                content {
                    contentType(APPLICATION_HAL_JSON)

                    jsonPath("$.id") { value(mealInfo.id.value) }
                    jsonPath("$.name") { value(mealInfo.name.value) }
                    jsonPath("$.description") { value(mealInfo.description.value) }
                    jsonPath("$.price") { value(mealInfo.price.value.setScale(1)) }
                    jsonPath("$.version") { value(mealInfo.version.value) }
                    jsonPath("$._links.self.href") { value(url) }
                    jsonPath("$._links.remove.href") { value(url) }
                }
            }

        getMealById.verifyInvoked(mealInfo.id)
    }

    @Configuration
    class TestConfiguration {

        @Bean
        fun getMealById() = MockGetMealById()

        @Bean
        fun getMenuEndpoint(getMealById: GetMealById) = GetMealByIdEndpoint(getMealById)
    }
}
