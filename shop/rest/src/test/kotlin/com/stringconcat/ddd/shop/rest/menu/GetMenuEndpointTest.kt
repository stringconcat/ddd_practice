package com.stringconcat.ddd.shop.rest.menu

import APPLICATION_HAL_JSON
import MockGetMenu
import com.stringconcat.ddd.shop.domain.meal
import com.stringconcat.ddd.shop.usecase.menu.GetMenu
import endpointUrl
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@WebMvcTest
@ContextConfiguration(classes = [GetMenuEndpointTest.TestConfiguration::class])
class GetMenuEndpointTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var getMenu: MockGetMenu

    @Test
    fun `get menu`() {
        val meal = getMenu.meal
        mockMvc.get("/rest/v1/menu")
            .andExpect {
                status { isOk() }
                content {
                    contentType(APPLICATION_HAL_JSON)

                    jsonPath("$._links.self.href") { value(endpointUrl("/menu")) }

                    jsonPath("$._embedded.meals.length()") { value(1) }
                    jsonPath("$._embedded.meals") { isNotEmpty() }
                    jsonPath("$._embedded.meals[0].id") { value(meal.id.value) }
                    jsonPath("$._embedded.meals[0].name") { value(meal.name.value) }
                    jsonPath("$._embedded.meals[0].description") { value(meal.description.value) }
                    jsonPath("$._embedded.meals[0].price") { value(meal.price.value.setScale(1)) }
                }
            }
    }

    @Configuration
    class TestConfiguration {

        @Bean
        fun getMenu() = MockGetMenu(meal = meal())

        @Bean
        fun getMenuEndpoint(getMenu: GetMenu) = GetMenuEndpoint(getMenu)
    }
}
