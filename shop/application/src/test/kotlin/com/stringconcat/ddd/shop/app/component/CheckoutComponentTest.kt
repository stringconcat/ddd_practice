package com.stringconcat.ddd.shop.app.component

import com.stringconcat.ddd.shop.app.ShopComponentTestConfiguration
import com.stringconcat.ddd.shop.app.TEST_TELNET_PORT
import com.stringconcat.ddd.shop.app.prepareCart
import com.stringconcat.ddd.shop.app.telnetClient
import com.stringconcat.ddd.shop.persistence.menu.InMemoryMealRepository
import com.stringconcat.ddd.shop.usecase.MockOrderExporter
import com.stringconcat.ddd.shop.usecase.cart.access.CartExtractor
import com.stringconcat.ddd.shop.usecase.order.access.ShopOrderExtractor
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [ShopComponentTestConfiguration::class], properties = ["telnet.port=$TEST_TELNET_PORT"])
class CheckoutComponentTest {

    @Autowired
    private lateinit var mealRepository: InMemoryMealRepository

    @Autowired
    private lateinit var cartExtractor: CartExtractor

    @Autowired
    private lateinit var orderExtractor: ShopOrderExtractor

    @Autowired
    private lateinit var orderExporter: MockOrderExporter

    @Test
    fun `when checkout cart must be removed`() {
        val client = telnetClient()
        val customerId = prepareCart(client, mealRepository)
        client.writeCommand("checkout street 1")

        client.readMessage() shouldContain "Please follow this URL"
        val cart = cartExtractor.getCart(customerId)
        cart.shouldBeNull()
    }

    @Test
    fun `when checkout order must be exported`() {
        val client = telnetClient()
        val customerId = prepareCart(client, mealRepository)
        client.writeCommand("checkout street 2")

        val message = client.readMessage()
        message shouldContain "Please follow this URL"

        val orderIdRes = Regex("#(\\d+)").find(message)
        orderIdRes.shouldNotBeNull()
        val createdOrderId = orderIdRes.groupValues[1].toLong()

        val order = orderExtractor.getLastOrder(customerId)
        order.shouldNotBeNull()
        order.id.toLongValue() shouldBe createdOrderId

        orderExporter.verifyInvoked(
            id = order.id,
            customerId = order.forCustomer,
            totalPrice = order.totalPrice()
        )
    }
}