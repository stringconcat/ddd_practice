package com.stringconcat.ddd.shop.usecase

import arrow.core.Either
import com.stringconcat.ddd.common.types.base.Version
import com.stringconcat.ddd.common.types.common.Address
import com.stringconcat.ddd.common.types.common.Count
import com.stringconcat.ddd.shop.domain.cart.Cart
import com.stringconcat.ddd.shop.domain.cart.CartId
import com.stringconcat.ddd.shop.domain.cart.CartRestorer
import com.stringconcat.ddd.shop.domain.cart.CustomerId
import com.stringconcat.ddd.shop.domain.menu.Meal
import com.stringconcat.ddd.shop.domain.menu.MealDescription
import com.stringconcat.ddd.shop.domain.menu.MealId
import com.stringconcat.ddd.shop.domain.menu.MealName
import com.stringconcat.ddd.shop.domain.menu.MealRestorer
import com.stringconcat.ddd.shop.domain.menu.Price
import com.stringconcat.ddd.shop.domain.order.ShopOrder
import com.stringconcat.ddd.shop.domain.order.ShopOrderId
import com.stringconcat.ddd.shop.domain.order.OrderItem
import com.stringconcat.ddd.shop.domain.order.ShopOrderRestorer
import com.stringconcat.ddd.shop.domain.order.OrderState
import com.stringconcat.ddd.shop.domain.order.CustomerHasActiveOrder
import com.stringconcat.ddd.shop.usecase.cart.CartExtractor
import com.stringconcat.ddd.shop.usecase.cart.CartPersister
import com.stringconcat.ddd.shop.usecase.cart.CartRemover
import com.stringconcat.ddd.shop.usecase.menu.MealExtractor
import com.stringconcat.ddd.shop.usecase.menu.MealPersister
import com.stringconcat.ddd.shop.usecase.order.ShopOrderExtractor
import com.stringconcat.ddd.shop.usecase.order.ShopOrderPersister
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.LinkedHashMap
import java.util.UUID
import kotlin.math.absoluteValue
import kotlin.random.Random

fun address(): Address {

    val result = Address.from(
        street = "${Random.nextInt()}th ave",
        building = Random.nextInt().absoluteValue
    )

    check(result is Either.Right<Address>)
    return result.b
}

fun mealName(): MealName {
    val result = MealName.from("Name ${Random.nextInt()}")
    check(result is Either.Right<MealName>)
    return result.b
}

fun mealDescription(): MealDescription {
    val result = MealDescription.from("Description ${Random.nextInt()}")
    check(result is Either.Right<MealDescription>)
    return result.b
}

fun price(value: BigDecimal = BigDecimal(Random.nextInt(1, 500000))): Price {
    val result = Price.from(value)
    check(result is Either.Right<Price>)
    return result.b
}

fun version() = Version.new()

fun mealId() = MealId(Random.nextLong())

fun meal(removed: Boolean = false): Meal {

    return MealRestorer.restoreMeal(
        id = mealId(),
        name = mealName(),
        removed = removed,
        description = mealDescription(),
        price = price(),
        version = version()
    )
}

fun removedMeal() = meal(removed = true)

fun customerId() = CustomerId(UUID.randomUUID().toString())

fun cartId() = CartId(Random.nextLong())

fun count(value: Int = Random.nextInt(20, 5000)): Count {
    val result = Count.from(value)
    check(result is Either.Right<Count>)
    return result.b
}

fun cart(
    meals: Map<MealId, Count> = emptyMap(),
    customerId: CustomerId = customerId()
): Cart {
    return CartRestorer.restoreCart(
        id = cartId(),
        forCustomer = customerId,
        created = OffsetDateTime.now(),
        meals = meals,
        version = version()
    )
}

fun orderId() = ShopOrderId(Random.nextLong())

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

fun order(
    state: OrderState = OrderState.COMPLETED,
    orderItems: Set<OrderItem> = emptySet(),
): ShopOrder {
    return ShopOrderRestorer.restoreOrder(
        id = orderId(),
        created = OffsetDateTime.now(),
        forCustomer = customerId(),
        orderItems = orderItems,
        address = address(),
        state = state,
        version = version()
    )
}

class TestMealPersister : HashMap<MealId, Meal>(), MealPersister {
    override fun save(meal: Meal) {
        this[meal.id] = meal
    }
}

class TestCartPersister : HashMap<CustomerId, Cart>(), CartPersister {
    override fun save(cart: Cart) {
        this[cart.forCustomer] = cart
    }
}

class TestMealExtractor : HashMap<MealId, Meal>(), MealExtractor {
    override fun getById(id: MealId) = this[id]

    override fun getByName(name: MealName): Meal? {
        return values.firstOrNull { it.name == name }
    }

    override fun getAll() = this.values.toList()
}

class TestCustomerHasActiveOrder(val hasActive: Boolean) : CustomerHasActiveOrder {
    override fun check(forCustomer: CustomerId): Boolean {
        return hasActive
    }
}

class TestCartExtractor : HashMap<CustomerId, Cart>(), CartExtractor {
    override fun getCart(forCustomer: CustomerId): Cart? = this[forCustomer]
}

class TestShopOrderPersister : ShopOrderPersister, HashMap<ShopOrderId, ShopOrder>() {
    override fun save(order: ShopOrder) {
        this[order.id] = order
    }
}

class TestShopOrderExtractor : ShopOrderExtractor, LinkedHashMap<ShopOrderId, ShopOrder>() {
    override fun getById(orderId: ShopOrderId) = this[orderId]

    override fun getLastOrder(forCustomer: CustomerId): ShopOrder? {
        return this.values.lastOrNull { it.forCustomer == forCustomer }
    }

    override fun getAll() = values.toList()
}

class TestCartRemover : CartRemover {
    internal val deleted = ArrayList<CartId>()
    override fun deleteCart(cart: Cart) {
        deleted.add(cart.id)
    }
}