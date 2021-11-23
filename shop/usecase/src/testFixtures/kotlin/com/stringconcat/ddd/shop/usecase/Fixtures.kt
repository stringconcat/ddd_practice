package com.stringconcat.ddd.shop.usecase

import com.stringconcat.ddd.common.types.common.Address
import com.stringconcat.ddd.common.types.common.Count
import com.stringconcat.ddd.common.types.count
import com.stringconcat.ddd.shop.domain.cart.Cart
import com.stringconcat.ddd.shop.domain.cart.CartId
import com.stringconcat.ddd.shop.domain.cart.CustomerId
import com.stringconcat.ddd.shop.domain.meal
import com.stringconcat.ddd.shop.domain.menu.Meal
import com.stringconcat.ddd.shop.domain.menu.MealDescription
import com.stringconcat.ddd.shop.domain.menu.MealId
import com.stringconcat.ddd.shop.domain.menu.MealName
import com.stringconcat.ddd.shop.domain.menu.MealRemovedFromMenuDomainEvent
import com.stringconcat.ddd.shop.domain.menu.Price
import com.stringconcat.ddd.shop.domain.order
import com.stringconcat.ddd.shop.domain.order.CustomerHasActiveOrder
import com.stringconcat.ddd.shop.domain.order.OrderState
import com.stringconcat.ddd.shop.domain.order.ShopOrder
import com.stringconcat.ddd.shop.domain.order.ShopOrderCancelledDomainEvent
import com.stringconcat.ddd.shop.domain.order.ShopOrderCompletedDomainEvent
import com.stringconcat.ddd.shop.domain.order.ShopOrderConfirmedDomainEvent
import com.stringconcat.ddd.shop.domain.order.ShopOrderId
import com.stringconcat.ddd.shop.domain.order.ShopOrderPaidDomainEvent
import com.stringconcat.ddd.shop.usecase.cart.access.CartExtractor
import com.stringconcat.ddd.shop.usecase.cart.access.CartPersister
import com.stringconcat.ddd.shop.usecase.cart.access.CartRemover
import com.stringconcat.ddd.shop.usecase.menu.access.MealExtractor
import com.stringconcat.ddd.shop.usecase.menu.access.MealPersister
import com.stringconcat.ddd.shop.usecase.order.access.ShopOrderExtractor
import com.stringconcat.ddd.shop.usecase.order.access.ShopOrderPersister
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.maps.shouldContainExactly
import io.kotest.matchers.shouldBe
import java.util.TreeMap

fun removedMeal() = meal(removed = true)

fun orderReadyForPay() = order(state = OrderState.WAITING_FOR_PAYMENT)

fun orderNotReadyForPay() = order(state = OrderState.COMPLETED)

fun orderReadyForCancel() = order(state = OrderState.PAID)

fun orderNotReadyForCancel() = order(state = OrderState.COMPLETED)

fun orderReadyForConfirm() = order(state = OrderState.PAID)

fun orderNotReadyForConfirm() = order(state = OrderState.WAITING_FOR_PAYMENT)

fun orderReadyForComplete() = order(state = OrderState.CONFIRMED)

fun orderNotReadyForComplete() = order(state = OrderState.CANCELLED)

fun activeOrder() = order(state = OrderState.CONFIRMED)

fun nonActiveOrder() = order(state = OrderState.CANCELLED)

class MockMealPersister : MealPersister {

    lateinit var meal: Meal

    override fun save(meal: Meal) {
        this.meal = meal
    }

    fun verifyInvoked(meal: Meal) {
        this.meal shouldBe meal
    }

    fun verifyInvoked(id: MealId, name: MealName, description: MealDescription, price: Price) {
        this.meal.id shouldBe id
        this.meal.name shouldBe name
        this.meal.description shouldBe description
        this.meal.price shouldBe price
    }

    fun verifyEventsAfterDeletion(id: MealId) {
        this.meal.popEvents() shouldContainExactly listOf(MealRemovedFromMenuDomainEvent(id))
    }

    fun verifyEmpty() {
        ::meal.isInitialized shouldBe false
    }
}

class MockCartPersister : CartPersister {

    lateinit var cart: Cart

    override fun save(cart: Cart) {
        this.cart = cart
    }

    fun verifyInvoked(cart: Cart) {
        this.cart shouldBe cart
    }

    fun verifyInvoked(cart: Cart, idMeal: MealId) {
        this.cart shouldBe cart
        this.cart.meals() shouldContainExactly mapOf(idMeal to count(1))
    }

    fun verifyInvoked(id: CartId, customerId: CustomerId, idMeal: MealId) {
        this.cart.id shouldBe id
        this.cart.forCustomer shouldBe customerId
        this.cart.meals() shouldContainExactly mapOf(idMeal to count(1))
    }

    fun verifyEmpty() {
        ::cart.isInitialized shouldBe false
    }
}

class MockShopOrderPersister : ShopOrderPersister {

    lateinit var order: ShopOrder

    override fun save(order: ShopOrder) {
        this.order = order
    }

    fun verifyInvoked(order: ShopOrder) {
        this.order shouldBe order
    }

    fun verifyInvoked(
        orderId: ShopOrderId, address: Address, customerId: CustomerId,
        mealId: MealId, countItems: Count, priceItems: Price
    ) {
        this.order.id shouldBe orderId
        this.order.address shouldBe address
        this.order.forCustomer shouldBe customerId
        this.order.orderItems.shouldHaveSize(1)

        val orderItem = this.order.orderItems.first()
        orderItem.mealId shouldBe mealId
        orderItem.count shouldBe countItems
        orderItem.price shouldBe priceItems
    }

    fun verifyEventsAfterCancellation(id: ShopOrderId) {
        this.order.popEvents() shouldContainExactly listOf(ShopOrderCancelledDomainEvent(id))
    }

    fun verifyEventsAfterCompletion(id: ShopOrderId) {
        this.order.popEvents() shouldContainExactly listOf(ShopOrderCompletedDomainEvent(id))
    }

    fun verifyEventsAfterConfirmation(id: ShopOrderId) {
        this.order.popEvents() shouldContainExactly listOf(ShopOrderConfirmedDomainEvent(id))
    }

    fun verifyEventsAfterPayment(id: ShopOrderId) {
        this.order.popEvents() shouldContainExactly listOf(ShopOrderPaidDomainEvent(id))
    }

    fun verifyPrice(price: Price) {
        this.order.totalPrice() shouldBe price
    }

    fun verifyEmpty() {
        ::order.isInitialized shouldBe false
    }
}

class MockCartRemover : CartRemover {

    lateinit var id: CartId

    override fun deleteCart(cart: Cart) {
        this.id = cart.id
    }

    fun verifyInvoked(cartId: CartId) {
        this.id shouldBe cartId
    }

    fun verifyEmpty() {
        ::id.isInitialized shouldBe false
    }
}

class MockCartExtractor : CartExtractor {

    lateinit var cart: Cart
    lateinit var forCustomer: CustomerId

    constructor()
    constructor(cart: Cart) {
        this.cart = cart
    }

    override fun getCart(forCustomer: CustomerId): Cart? {
        this.forCustomer = forCustomer
        return if (::cart.isInitialized) this.cart else null
    }

    fun verifyInvoked(forCustomer: CustomerId) {
        this.forCustomer shouldBe forCustomer
    }

    fun verifyEmpty() {
        ::forCustomer.isInitialized shouldBe false
    }
}

class MockCustomerHasActiveOrder(val hasActive: Boolean) : CustomerHasActiveOrder {

    lateinit var forCustomer: CustomerId

    override fun check(forCustomer: CustomerId): Boolean {
        this.forCustomer = forCustomer
        return hasActive
    }

    fun verifyInvoked(forCustomer: CustomerId) {
        this.forCustomer shouldBe forCustomer
    }

    fun verifyEmpty() {
        ::forCustomer.isInitialized shouldBe false
    }
}

class MockMealExtractor : MealExtractor {

    lateinit var meal: Meal

    lateinit var id: MealId
    lateinit var name: MealName
    var all: Boolean = false

    constructor()
    constructor(meal: Meal) {
        this.meal = meal
    }

    override fun getById(id: MealId): Meal? {
        this.id = id
        return if (::meal.isInitialized && this.meal.id == id) this.meal else null
    }

    override fun getByName(name: MealName): Meal? {
        this.name = name
        return if (::meal.isInitialized && this.meal.name == name) this.meal else null
    }

    override fun getAll(): List<Meal> {
        this.all = true
        return if (::meal.isInitialized) return listOf(this.meal) else emptyList()
    }

    fun verifyInvokedGetById(id: MealId) {
        this.id shouldBe id
        this.all shouldBe false
        ::name.isInitialized shouldBe false
    }

    fun verifyInvokedGetByName(name: MealName) {
        this.name shouldBe name
        this.all shouldBe false
        ::id.isInitialized shouldBe false
    }

    fun verifyInvokedGetAll() {
        this.all shouldBe true
        ::id.isInitialized shouldBe false
        ::name.isInitialized shouldBe false
    }

    fun verifyEmpty() {
        this.all shouldBe false
        ::id.isInitialized shouldBe false
        ::name.isInitialized shouldBe false
    }
}

class TestShopOrderExtractor : ShopOrderExtractor,
    TreeMap<ShopOrderId, ShopOrder>({ k1, k2 -> k1.value.compareTo(k2.value) }) {
    override fun getById(orderId: ShopOrderId) = this[orderId]

    override fun getLastOrder(forCustomer: CustomerId): ShopOrder? {
        return this.values.lastOrNull { it.forCustomer == forCustomer }
    }

    override fun getAll(startId: ShopOrderId, limit: Int) =
        tailMap(startId).toList().take(limit).map { it.second }
}