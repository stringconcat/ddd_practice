package com.stringconcat.ddd.shop.rest.menu

import APPLICATION_HAL_FORMS_JSON
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
import org.springframework.hateoas.config.EnableHypermediaSupport
import org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType
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

        val url = API_V1_MENU_GET_BY_ID.withId(mealId().toLongValue()).withHost()
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

        val url = API_V1_MENU_GET_BY_ID.withId(mealInfo.id.toLongValue()).withHost()
        mockMvc.get(url)
            .andExpect {
                status { isOk() }
                content {
                    contentType(APPLICATION_HAL_FORMS_JSON)

                    jsonPath("$.id") { value(mealInfo.id.toLongValue()) }
                    jsonPath("$.name") { value(mealInfo.name.toStringValue()) }
                    jsonPath("$.description") { value(mealInfo.description.toStringValue()) }
                    jsonPath("$.price") { value(mealInfo.price.toBigDecimalValue().setScale(1)) }
                    jsonPath("$.version") { value(mealInfo.version.toLongValue()) }
                    jsonPath("$._links.self.href") { value(url) }
                    jsonPath("$._links.remove.href") { value(url) }
                }
            }

        getMealById.verifyInvoked(mealInfo.id)
    }

    @Configuration
    @EnableHypermediaSupport(type = [HypermediaType.HAL_FORMS])
    class TestConfiguration {

        @Bean
        fun getMealById() = MockGetMealById()

        @Bean
        fun getMenuEndpoint(getMealById: GetMealById) = GetMealByIdEndpoint(getMealById)
    }
}
