package com.stringconcat.ddd.order.domain.cart

import com.stringconcat.ddd.order.domain.cart
import com.stringconcat.ddd.order.domain.cartId
import com.stringconcat.ddd.order.domain.count
import com.stringconcat.ddd.order.domain.customerId
import com.stringconcat.ddd.order.domain.meal
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.date.shouldBeBefore
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.maps.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.time.OffsetDateTime

internal class CartTest {

    @Test
    fun `create cart - success`() {

        val customerId = customerId()
        val cart = Cart.create(TestCartIdGenerator, customerId)

        val id = TestCartIdGenerator.id
        cart.id shouldBe id
        cart.forCustomer shouldBe customerId
        cart.meals().shouldBeEmpty()
        cart.created shouldBeBefore OffsetDateTime.now()
        cart.popEvents() shouldContainExactly listOf(CartCreatedDomainEvent(id))
    }

    @Test
    fun `add meal - no meal in cart (success)`() {

        val cart = cart()
        val meal = meal()

        cart.addMeal(meal)
        cart.popEvents() shouldContainExactly listOf(MealAddedToCartDomainEvent(cart.id, meal.id))
        cart.meals() shouldContainExactly mapOf(meal.id to count(1))
    }

    @Test
    fun `add meal - has meals in cart (success)`() {

        val meal = meal()
        val count = count(2)
        val cart = cart(meals = mapOf(meal.id to count))

        cart.addMeal(meal)
        cart.popEvents() shouldContainExactly listOf(MealAddedToCartDomainEvent(cart.id, meal.id))
        cart.meals() shouldContainExactly mapOf(meal.id to count(3))
    }

    @Test
    fun `remove meal - cart is empty (success)`() {
        val meal = meal()
        val cart = cart()
        cart.removeMeals(meal.id)
        cart.popEvents().shouldBeEmpty()
    }

    @Test
    fun `remove meal - meal not in cart (success)`() {
        val existingMeal = meal()
        val count = count(12)
        val nonExistingMeal = meal()
        val meals = mapOf(existingMeal.id to count)

        val cart = cart(meals = meals)

        cart.removeMeals(nonExistingMeal.id)
        cart.popEvents().shouldBeEmpty()
        cart.meals() shouldContainExactly meals
    }

    @Test
    fun `remove meal - meal in cart (success)`() {

        val mealForRemoving = meal()
        val removingCount = count(12)

        val meal = meal()
        val count = count(10)

        val meals = mapOf(mealForRemoving.id to removingCount, meal.id to count)
        val cart = cart(meals = meals)

        cart.removeMeals(mealForRemoving.id)
        cart.popEvents() shouldContainExactly listOf(MealRemovedFromCartDomainEvent(cart.id, mealForRemoving.id))
        cart.meals() shouldContainExactly mapOf(meal.id to count)
    }

    object TestCartIdGenerator : CartIdGenerator {
        val id = cartId()
        override fun generate() = id
    }
}