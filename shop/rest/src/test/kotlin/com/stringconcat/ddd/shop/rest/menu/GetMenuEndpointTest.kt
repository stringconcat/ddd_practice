package com.stringconcat.ddd.shop.rest.menu

import APPLICATION_HAL_FORMS_JSON
import MockGetMenu
import com.stringconcat.ddd.shop.rest.API_V1_MENU_DELETE_BY_ID
import com.stringconcat.ddd.shop.rest.API_V1_MENU_GET_ALL
import com.stringconcat.ddd.shop.rest.API_V1_MENU_GET_BY_ID
import com.stringconcat.ddd.shop.usecase.menu.GetMenu
import mealInfo
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.hateoas.config.EnableHypermediaSupport
import org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import withHost
import withId

@WebMvcTest
@ContextConfiguration(classes = [GetMenuEndpointTest.TestConfiguration::class])
class GetMenuEndpointTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var getMenu: MockGetMenu

    @Test
    fun `get menu`() {
        val meal = getMenu.mealInfo

        val url = API_V1_MENU_GET_ALL.withHost()
        mockMvc.get(url)
            .andExpect {
                status { isOk() }
                content {
                    contentType(APPLICATION_HAL_FORMS_JSON)

                    jsonPath("$._links.self.href") { value(url) }

                    jsonPath("$._embedded.meals.length()") { value(1) }
                    jsonPath("$._embedded.meals") { isNotEmpty() }
                    jsonPath("$._embedded.meals[0].id") { value(meal.id.toLongValue()) }
                    jsonPath("$._embedded.meals[0].name") { value(meal.name.toStringValue()) }
                    jsonPath("$._embedded.meals[0].description") { value(meal.description.toStringValue()) }
                    jsonPath("$._embedded.meals[0].price") { value(meal.price.toBigDecimalValue().setScale(1)) }
                    jsonPath("$._embedded.meals[0].version") { value(meal.version.toLongValue()) }

                    jsonPath("$._embedded.meals[0]._links.self.href") {
                        value(API_V1_MENU_GET_BY_ID.withId(meal.id.toLongValue()).withHost())
                    }

                    jsonPath("$._embedded.meals[0]._links.remove.href") {
                        value(API_V1_MENU_DELETE_BY_ID.withId(meal.id.toLongValue()).withHost())
                    }
                    jsonPath("$._embedded.meals[0]._templates") { exists() }
                }
            }
    }

    @Configuration
    @EnableHypermediaSupport(type = [HypermediaType.HAL_FORMS])
    class TestConfiguration {

        @Bean
        fun getMenu() = MockGetMenu(mealInfo = mealInfo())

        @Bean
        fun getMenuEndpoint(getMenu: GetMenu) = GetMenuEndpoint(getMenu)
    }
}
