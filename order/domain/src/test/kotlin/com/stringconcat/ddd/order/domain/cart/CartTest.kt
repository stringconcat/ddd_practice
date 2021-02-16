package com.stringconcat.ddd.order.domain.cart

import com.stringconcat.ddd.order.domain.TestCustomerHasActiveOrderRule
import com.stringconcat.ddd.order.domain.cart
import com.stringconcat.ddd.order.domain.count
import com.stringconcat.ddd.order.domain.meal
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.maps.shouldContainExactly
import org.junit.jupiter.api.Test

internal class CartTest {

    private val activeOrderForCustomer = TestCustomerHasActiveOrderRule(false)

    @Test
    fun `add meal - no meal in cart (success)`() {

        val cart = cart()
        val meal = meal()

        val result = cart.addMeal(meal, activeOrderForCustomer)
        result shouldBeRight Unit
        cart.popEvents() shouldContainExactly listOf(MealHasBeenAddedToCart(cart.id, meal.id))
        cart.meals() shouldContainExactly mapOf(meal.id to count(1))
    }

    @Test
    fun `add meal - has meals in cart (success)`() {

        val meal = meal()
        val count = count(2)
        val cart = cart(meals = mapOf(meal.id to count))

        val result = cart.addMeal(meal, activeOrderForCustomer)
        result shouldBeRight Unit

        cart.popEvents() shouldContainExactly listOf(MealHasBeenAddedToCart(cart.id, meal.id))
        cart.meals() shouldContainExactly mapOf(meal.id to count(3))
    }

    @Test
    fun `add meal - has active order`() {

        val cart = cart()
        val meal = meal()

        val activeOrderForCustomer = TestCustomerHasActiveOrderRule(true)
        val result = cart.addMeal(meal, activeOrderForCustomer)
        result shouldBeLeft AddMealToCartError.HasActiveOrder
        cart.popEvents() shouldContainExactly emptyList()
    }

    @Test
    fun `add meal - limit reached`() {
        val meal = meal()
        val cart = cart(meals = mapOf(meal.id to count(Int.MAX_VALUE)))

        val result = cart.addMeal(meal, activeOrderForCustomer)
        result shouldBeLeft AddMealToCartError.LimitReached
        cart.popEvents() shouldContainExactly emptyList()
    }

    @Test
    fun `remove meal - cart is empty (success)`() {
        val meal = meal()
        val cart = cart()
        cart.removeMeals(meal.id)
        cart.popEvents() shouldContainExactly emptyList()
    }

    @Test
    fun `remove meal - meal not in cart (success)`() {
        val existingMeal = meal()
        val count = count(12)
        val nonExistingMeal = meal()
        val meals = mapOf(existingMeal.id to count)

        val cart = cart(meals = meals)

        cart.removeMeals(nonExistingMeal.id)
        cart.popEvents() shouldContainExactly emptyList()
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
        cart.popEvents() shouldContainExactly listOf(MealHasBeenRemovedFromCart(cart.id, mealForRemoving.id))
        cart.meals() shouldContainExactly mapOf(meal.id to count)
    }
}