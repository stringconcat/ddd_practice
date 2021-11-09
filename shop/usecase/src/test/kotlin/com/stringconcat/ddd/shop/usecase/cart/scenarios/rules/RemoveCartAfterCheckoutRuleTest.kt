package com.stringconcat.ddd.shop.usecase.cart.scenarios.rules

import com.stringconcat.ddd.shop.domain.cart
import com.stringconcat.ddd.shop.domain.customerId
import com.stringconcat.ddd.shop.domain.order.ShopOrderCreatedDomainEvent
import com.stringconcat.ddd.shop.domain.orderId
import com.stringconcat.ddd.shop.usecase.TestCartExtractor
import com.stringconcat.ddd.shop.usecase.TestCartRemover
import com.stringconcat.ddd.shop.usecase.cart.rules.RemoveCartAfterCheckoutRule
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import org.junit.jupiter.api.Test

internal class RemoveCartAfterCheckoutRuleTest {

    @Test
    fun `successfully removed`() {

        val cartRemover = TestCartRemover()
        val cart = cart()
        val cartExtractor = TestCartExtractor().apply {
            this[cart.forCustomer] = cart
        }

        val rule = RemoveCartAfterCheckoutRule(cartExtractor, cartRemover)
        val event = ShopOrderCreatedDomainEvent(orderId(), cart.forCustomer)

        rule.handle(event)
        cartRemover.deleted shouldContainExactly listOf(cart.id)
    }

    @Test
    fun `cart not found`() {

        val cartRemover = TestCartRemover()
        val cartExtractor = TestCartExtractor()
        val rule = RemoveCartAfterCheckoutRule(cartExtractor, cartRemover)
        val event = ShopOrderCreatedDomainEvent(orderId(), customerId())

        rule.handle(event)
        cartRemover.deleted.shouldBeEmpty()
    }
}