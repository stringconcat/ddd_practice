package com.stringconcat.dev.course.app.component

import com.stringconcat.ddd.shop.persistence.menu.InMemoryMealRepository
import com.stringconcat.dev.course.app.ShopComponentTestConfiguration
import com.stringconcat.dev.course.app.TEST_TELNET_PORT
import com.stringconcat.dev.course.app.prepareCart
import com.stringconcat.dev.course.app.telnetClient
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [ShopComponentTestConfiguration::class], properties = ["telnet.port=$TEST_TELNET_PORT"])
class TelnetComponentTest {

    @Autowired
    private lateinit var mealRepository: InMemoryMealRepository

    @Test
    fun `two telnet clients makes different session`() {
        val first = telnetClient()
        val second = telnetClient()
        val firstCustomerId = prepareCart(client = first, mealRepository = mealRepository)
        val secondCustomerId = prepareCart(client = second, mealRepository = mealRepository)

        firstCustomerId shouldNotBe secondCustomerId
    }
}