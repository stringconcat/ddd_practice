package com.stringconcat.dev.course.app.component

import com.stringconcat.ddd.shop.domain.meal
import com.stringconcat.ddd.shop.persistence.menu.InMemoryMealRepository
import com.stringconcat.ddd.shop.usecase.cart.access.CartExtractor
import com.stringconcat.dev.course.app.ShopComponentTestConfiguration
import com.stringconcat.dev.course.app.TEST_TELNET_PORT
import com.stringconcat.dev.course.app.prepareCart
import com.stringconcat.dev.course.app.telnetClient
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.maps.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [ShopComponentTestConfiguration::class], properties = ["telnet.port=$TEST_TELNET_PORT"])
class AddMealToCartComponentTest {
    @Autowired
    private lateinit var mealRepository: InMemoryMealRepository

    @Autowired
    private lateinit var cartExtractor: CartExtractor

    @Test
    fun `when add cart must contain a meal`() {
        val client = telnetClient()
        val meal = meal()
        val customerId = prepareCart(client, mealRepository, meal)
        val cart = cartExtractor.getCart(customerId)

        cart.shouldNotBeNull()
        val meals = cart.meals()

        meals shouldHaveSize 1
        meals.keys.shouldContainExactly(meal.id)
    }
}