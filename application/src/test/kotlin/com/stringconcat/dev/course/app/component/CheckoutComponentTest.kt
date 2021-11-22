package com.stringconcat.dev.course.app.component

import com.stringconcat.ddd.shop.domain.cart.CustomerId
import com.stringconcat.ddd.shop.domain.meal
import com.stringconcat.ddd.shop.persistence.menu.InMemoryMealRepository
import com.stringconcat.ddd.shop.usecase.cart.access.CartExtractor
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [ShopComponentTestConfiguration::class], properties = ["telnet.port=22121"])
class CheckoutComponentTest {

    companion object {
        const val SUCCESS_RESPONSE = "\r\nOK"
    }

    @Autowired
    private lateinit var client: TestTelnetClient

    @Autowired
    private lateinit var mealRepository: InMemoryMealRepository

    @Autowired
    private lateinit var cartExtractor: CartExtractor

    @Test
    fun `when checkout cart must be removed`() {
        val customerId = prepareCart()

        client.writeCommand("checkout street 1")

        client.readMessage() shouldContain "Please follow this URL"
        val cart = cartExtractor.getCart(customerId)
        cart.shouldBeNull()
    }

    private fun prepareCart(): CustomerId {
        val meal = meal()
        mealRepository.save(meal)

        client.writeCommand("add ${meal.id.value}")
        client.readMessage() shouldBe SUCCESS_RESPONSE

        val uuidRegex = "\\w{8}-\\w{4}-\\w{4}-\\w{4}-\\w{12}".toRegex()
        client.writeCommand("cart")
        val customerString = uuidRegex.find(client.readMessage())?.value
        customerString.shouldNotBeNull()

        return CustomerId(customerString)
    }
}