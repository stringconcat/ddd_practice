package com.stringconcat.ddd.shop.usecase.cart.rules

import com.stringconcat.ddd.shop.domain.cart
import com.stringconcat.ddd.shop.domain.customerId
import com.stringconcat.ddd.shop.domain.order.ShopOrderCreatedDomainEvent
import com.stringconcat.ddd.shop.domain.orderId
import com.stringconcat.ddd.shop.domain.price
import com.stringconcat.ddd.shop.usecase.MockCartExtractor
import com.stringconcat.ddd.shop.usecase.MockCartRemover
import org.junit.jupiter.api.Test

internal class RemoveCartAfterCheckoutRuleTest {

    @Test
    fun `successfully removed`() {

        val cartRemover = MockCartRemover()
        val cart = cart()

        val cartExtractor = MockCartExtractor(cart)

        val rule = RemoveCartAfterCheckoutRule(cartExtractor, cartRemover)
        val event = ShopOrderCreatedDomainEvent(orderId(), cart.forCustomer, price())

        rule.handle(event)

        cartExtractor.verifyInvoked(cart.forCustomer)
        cartRemover.verifyInvoked(cart.id)
    }

    @Test
    fun `cart not found`() {

        val cartRemover = MockCartRemover()
        val cartExtractor = MockCartExtractor()
        val rule = RemoveCartAfterCheckoutRule(cartExtractor, cartRemover)
        val customerId = customerId()
        val event = ShopOrderCreatedDomainEvent(orderId(), customerId, price())

        rule.handle(event)

        cartExtractor.verifyInvoked(customerId)
        cartRemover.verifyEmpty()
    }
}