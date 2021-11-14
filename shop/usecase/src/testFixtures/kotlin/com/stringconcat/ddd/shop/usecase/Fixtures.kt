package com.stringconcat.ddd.shop.usecase

import com.stringconcat.ddd.shop.domain.cart.Cart
import com.stringconcat.ddd.shop.domain.cart.CartId
import com.stringconcat.ddd.shop.domain.cart.CustomerId
import com.stringconcat.ddd.shop.domain.meal
import com.stringconcat.ddd.shop.domain.menu.Meal
import com.stringconcat.ddd.shop.domain.menu.MealId
import com.stringconcat.ddd.shop.domain.menu.MealName
import com.stringconcat.ddd.shop.domain.order
import com.stringconcat.ddd.shop.domain.order.CustomerHasActiveOrder
import com.stringconcat.ddd.shop.domain.order.OrderState
import com.stringconcat.ddd.shop.domain.order.ShopOrder
import com.stringconcat.ddd.shop.domain.order.ShopOrderId
import com.stringconcat.ddd.shop.usecase.cart.access.CartExtractor
import com.stringconcat.ddd.shop.usecase.cart.access.CartPersister
import com.stringconcat.ddd.shop.usecase.cart.access.CartRemover
import com.stringconcat.ddd.shop.usecase.menu.access.MealExtractor
import com.stringconcat.ddd.shop.usecase.menu.access.MealPersister
import com.stringconcat.ddd.shop.usecase.order.access.ShopOrderExtractor
import com.stringconcat.ddd.shop.usecase.order.access.ShopOrderPersister
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

class TestShopOrderExtractor : ShopOrderExtractor,
    TreeMap<ShopOrderId, ShopOrder>({ k1, k2 -> k1.value.compareTo(k2.value) }) {
    override fun getById(orderId: ShopOrderId) = this[orderId]

    override fun getLastOrder(forCustomer: CustomerId): ShopOrder? {
        return this.values.lastOrNull { it.forCustomer == forCustomer }
    }

    override fun getAll(startId: ShopOrderId, limit: Int) =
        tailMap(startId).toList().take(limit).map { it.second }
}

class TestCartRemover : CartRemover {
    val deleted = ArrayList<CartId>()
    override fun deleteCart(cart: Cart) {
        deleted.add(cart.id)
    }
}