package com.stringconcat.ddd.shop.rest.menu

import com.stringconcat.ddd.shop.domain.meal
import com.stringconcat.ddd.shop.domain.menu.Meal
import com.stringconcat.ddd.shop.usecase.menu.GetMenu
import com.stringconcat.ddd.shop.usecase.menu.MealInfo
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
    lateinit var getMenu: TestGetMenu

    @Test
    fun `get menu`() {
        val meal = getMenu.meal
        mockMvc.get("/menu")
            .andExpect {
                status { is2xxSuccessful() }
                content {
                    jsonPath("$._embedded.meals") { isArray() }
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
        fun getMenu() = TestGetMenu(meal = meal())

        @Bean
        fun getMenuEndpoint(getMenu: GetMenu) = GetMenuEndpoint(getMenu)
    }

    class TestGetMenu(val meal: Meal) : GetMenu {
        override fun execute() = listOf(MealInfo(id = meal.id,
            name = meal.name,
            description = meal.description,
            price = meal.price))
    }
}
