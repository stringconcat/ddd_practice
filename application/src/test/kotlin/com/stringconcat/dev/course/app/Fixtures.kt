package com.stringconcat.dev.course.app

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.stringconcat.ddd.common.types.base.Version
import com.stringconcat.ddd.common.types.common.Address
import com.stringconcat.ddd.common.types.common.Count
import com.stringconcat.ddd.order.domain.cart.CustomerId
import com.stringconcat.ddd.order.domain.menu.Meal
import com.stringconcat.ddd.order.domain.menu.MealDescription
import com.stringconcat.ddd.order.domain.menu.MealId
import com.stringconcat.ddd.order.domain.menu.MealName
import com.stringconcat.ddd.order.domain.menu.MealRestorer
import com.stringconcat.ddd.order.domain.menu.Price
import com.stringconcat.ddd.order.domain.order.CustomerOrder
import com.stringconcat.ddd.order.domain.order.CustomerOrderId
import com.stringconcat.ddd.order.domain.order.CustomerOrderRestorer
import com.stringconcat.ddd.order.domain.order.OrderItem
import com.stringconcat.ddd.order.domain.order.OrderState
import com.stringconcat.ddd.order.usecase.menu.MealExtractor
import com.stringconcat.ddd.order.usecase.order.CrmProvider
import com.stringconcat.ddd.order.usecase.order.CrmSendHandlerError
import com.stringconcat.ddd.order.usecase.order.CustomerOrderExtractor
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.LinkedHashMap
import java.util.UUID
import kotlin.math.absoluteValue
import kotlin.random.Random

fun mealName(name: String = "Name ${Random.nextInt()}"): MealName {
    val result = MealName.from(name)
    check(result is Either.Right<MealName>)
    return result.b
}

fun orderId() = CustomerOrderId(Random.nextLong())

fun mealDescription(description: String = "Description ${Random.nextInt()}"): MealDescription {
    val result = MealDescription.from(description)
    check(result is Either.Right<MealDescription>)
    return result.b
}

fun price(value: BigDecimal = BigDecimal(Random.nextInt(1, 500000))): Price {
    val result = Price.from(value)
    check(result is Either.Right<Price>)
    return result.b
}

fun mealId(id: Long = Random.nextLong()) = MealId(id)

fun count(value: Int = Random.nextInt(20, 5000)): Count {
    val result = Count.from(value)
    check(result is Either.Right<Count>)
    return result.b
}

fun customerId() = CustomerId(UUID.randomUUID().toString())

fun address(): Address {

    val result = Address.from(
        street = "${Random.nextInt()}th ave",
        building = Random.nextInt().absoluteValue
    )

    check(result is Either.Right<Address>)
    return result.b
}

fun customerOrder(
    state: OrderState = OrderState.COMPLETED,
    orderItems: Set<OrderItem> = emptySet(),
): CustomerOrder {
    return CustomerOrderRestorer.restoreOrder(
        id = orderId(),
        created = OffsetDateTime.now(),
        forCustomer = customerId(),
        orderItems = orderItems,
        address = address(),
        state = state,
        version = Version.new()
    )
}

fun meal(removed: Boolean = false): Meal {

    return MealRestorer.restoreMeal(
        id = mealId(),
        name = mealName(),
        removed = removed,
        description = mealDescription(),
        price = price(),
        version = Version.new()
    )
}

class TestCustomerOrderExtractor : CustomerOrderExtractor, LinkedHashMap<CustomerOrderId, CustomerOrder>() {
    override fun getById(orderId: CustomerOrderId) = this[orderId]

    override fun getLastOrder(forCustomer: CustomerId): CustomerOrder? {
        return this.values.lastOrNull { it.forCustomer == forCustomer }
    }

    override fun getAll() = values.toList()
}

class TestMealExtractor : HashMap<MealId, Meal>(), MealExtractor {
    override fun getById(id: MealId) = this[id]

    override fun getByName(name: MealName): Meal? {
        return values.firstOrNull { it.name == name }
    }

    override fun getAll() = this.values.toList()
}

class TestSuccessfulCrmProvider : CrmProvider {
    lateinit var instance: Any

    override fun send(orderId: CustomerOrderId, price: Price): Either<CrmSendHandlerError, Unit> {
        this.instance = true
        return Unit.right()
    }

    fun verifyZeroInteraction() {
        ::instance.isInitialized.shouldBeFalse()
    }

    fun verifyNotZeroInteraction() {
        ::instance.isInitialized.shouldBeTrue()
    }
}

class TestFailedCrmProvider : CrmProvider {
    lateinit var instance: Any

    override fun send(orderId: CustomerOrderId, price: Price): Either<CrmSendHandlerError, Unit> {
        this.instance = true
        return CrmSendHandlerError.OrderNotSentToCrm.left()
    }

    fun verifyNotZeroInteraction() {
        ::instance.isInitialized.shouldBeTrue()
    }
}