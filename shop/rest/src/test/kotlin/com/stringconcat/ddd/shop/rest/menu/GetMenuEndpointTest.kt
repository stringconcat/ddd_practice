package com.stringconcat.ddd.shop.rest.menu

import MockGetMenu
import com.stringconcat.ddd.shop.rest.API_V1_MENU_GET_ALL
import com.stringconcat.ddd.shop.usecase.menu.GetMenu
import mealInfo
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import withHost

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
                    contentType(APPLICATION_JSON)

                    jsonPath("$.length()") { value(1) }
                    jsonPath("$.[0].id") { value(meal.id.toLongValue()) }
                    jsonPath("$.[0].name") { value(meal.name.toStringValue()) }
                    jsonPath("$.[0].description") { value(meal.description.toStringValue()) }
                    jsonPath("$.[0].price") { value(meal.price.toBigDecimalValue().setScale(1)) }
                    jsonPath("$.[0].version") { value(meal.version.toLongValue()) }
                }
            }
    }

    @Configuration
    class TestConfiguration {

        @Bean
        fun getMenu() = MockGetMenu(mealInfo = mealInfo())

        @Bean
        fun getMenuEndpoint(getMenu: GetMenu) = GetMenuEndpoint(getMenu)
    }
}
