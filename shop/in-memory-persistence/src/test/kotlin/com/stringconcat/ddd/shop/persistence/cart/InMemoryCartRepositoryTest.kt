package com.stringconcat.ddd.shop.persistence.cart

import com.stringconcat.ddd.shop.domain.cart
import com.stringconcat.ddd.shop.domain.cart.MealAddedToCartDomainEvent
import com.stringconcat.ddd.shop.domain.customerId
import com.stringconcat.ddd.shop.persistence.TestEventPublisher
import com.stringconcat.ddd.shop.persistence.cartWithEvents
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.maps.shouldContainExactly
import io.kotest.matchers.maps.shouldNotBeEmpty
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.matchers.types.shouldNotBeSameInstanceAs
import org.junit.jupiter.api.Test

class InMemoryCartRepositoryTest {

    @Test
    fun `saving cart - cart doesn't exist`() {
        val eventPublisher = TestEventPublisher()
        val repository = InMemoryCartRepository(eventPublisher)
        val cart = cartWithEvents()

        repository.save(cart)

        val storedCart = repository.storage[cart.forCustomer]
        storedCart shouldNotBeSameInstanceAs cart
        eventPublisher.storage.shouldHaveSize(1)

        val event = eventPublisher.storage.first()
        event.shouldBeInstanceOf<MealAddedToCartDomainEvent>()
        event.cartId shouldBe cart.id
    }

    @Test
    fun `saving cart - cart exists`() {

        val customerId = customerId()
        val existingCart = cart(customerId = customerId)

        val eventPublisher = TestEventPublisher()
        val repository = InMemoryCartRepository(eventPublisher)
        repository.storage[customerId] = existingCart

        val updatedCart = cartWithEvents()
        repository.save(updatedCart)
        repository.storage[customerId] = updatedCart

        val event = eventPublisher.storage.first()
        event.shouldBeInstanceOf<MealAddedToCartDomainEvent>()
        event.cartId shouldBe updatedCart.id
    }

    @Test
    fun `get by id - cart exists`() {
        val customerId = customerId()
        val existingCart = cart(customerId = customerId)

        val eventPublisher = TestEventPublisher()
        val repository = InMemoryCartRepository(eventPublisher)
        repository.storage[customerId] = existingCart

        val cart = repository.getCart(customerId)

        cart.shouldNotBeNull()

        cart shouldNotBeSameInstanceAs existingCart
        cart.id shouldBe existingCart.id
        cart.forCustomer shouldBe existingCart.forCustomer
        cart.created shouldBe existingCart.created
        cart.meals() shouldContainExactly existingCart.meals()
    }

    @Test
    fun `get by id - cart doesn't exist`() {
        val eventPublisher = TestEventPublisher()
        val repository = InMemoryCartRepository(eventPublisher)
        val cart = repository.getCart(customerId())
        cart.shouldBeNull()
    }

    @Test
    fun `delete cart - cart exists`() {

        val existingCart = cart()
        val eventPublisher = TestEventPublisher()
        val repository = InMemoryCartRepository(eventPublisher)
        repository.storage[existingCart.forCustomer] = existingCart

        repository.deleteCart(existingCart)
        repository.storage.shouldBeEmpty()
    }

    @Test
    fun `delete cart - cart doesn't exist`() {

        val cart = cart()
        val eventPublisher = TestEventPublisher()
        val repository = InMemoryCartRepository(eventPublisher)

        repository.deleteCart(cart)
        repository.storage.shouldBeEmpty()
    }

    @Test
    fun `copy cart test`() {
        val src = cartWithEvents()
        src.meals().shouldNotBeEmpty()

        val copy = src.copy()

        src shouldNotBeSameInstanceAs copy
        src.id shouldBe copy.id
        src.forCustomer shouldBe copy.forCustomer
        src.created shouldBe copy.created
        src.meals() shouldContainExactly copy.meals()
        src.meals() shouldNotBeSameInstanceAs copy.meals()
    }
}