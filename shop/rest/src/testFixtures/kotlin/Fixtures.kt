import arrow.core.Either
import com.stringconcat.ddd.shop.domain.meal
import com.stringconcat.ddd.shop.domain.menu.MealDescription
import com.stringconcat.ddd.shop.domain.menu.MealId
import com.stringconcat.ddd.shop.domain.menu.MealName
import com.stringconcat.ddd.shop.domain.menu.Price
import com.stringconcat.ddd.shop.domain.order
import com.stringconcat.ddd.shop.domain.order.OrderState
import com.stringconcat.ddd.shop.domain.order.ShopOrderId
import com.stringconcat.ddd.shop.usecase.menu.AddMealToMenu
import com.stringconcat.ddd.shop.usecase.menu.AddMealToMenuUseCaseError
import com.stringconcat.ddd.shop.usecase.menu.GetMealById
import com.stringconcat.ddd.shop.usecase.menu.GetMealByIdUseCaseError
import com.stringconcat.ddd.shop.usecase.menu.GetMenu
import com.stringconcat.ddd.shop.usecase.menu.RemoveMealFromMenu
import com.stringconcat.ddd.shop.usecase.menu.RemoveMealFromMenuUseCaseError
import com.stringconcat.ddd.shop.usecase.menu.dto.MealInfo
import com.stringconcat.ddd.shop.usecase.order.CancelOrder
import com.stringconcat.ddd.shop.usecase.order.CancelOrderUseCaseError
import com.stringconcat.ddd.shop.usecase.order.ConfirmOrder
import com.stringconcat.ddd.shop.usecase.order.ConfirmOrderUseCaseError
import com.stringconcat.ddd.shop.usecase.order.GetOrderById
import com.stringconcat.ddd.shop.usecase.order.GetOrderByIdUseCaseError
import com.stringconcat.ddd.shop.usecase.order.GetOrders
import com.stringconcat.ddd.shop.usecase.order.GetOrdersUseCaseError
import com.stringconcat.ddd.shop.usecase.order.dto.OrderDetails
import com.stringconcat.ddd.shop.usecase.order.dto.toDetails
import io.kotest.matchers.shouldBe

const val APPLICATION_HAL_JSON = "application/hal+json"
const val API_V1_TYPE_BASE_URL = "http://localhost"

fun errorTypeUrl(suffix: String) = "$API_V1_TYPE_BASE_URL/$suffix"
fun notFoundTypeUrl() = errorTypeUrl("not_found")
fun badRequestTypeUrl() = errorTypeUrl("bad_request")

fun String.withHost() = API_V1_TYPE_BASE_URL.plus(this)
fun String.withParameter(name: String, value: Any) = this.replace("{$name}", value.toString())

fun String.withId(id: Long) = this.withParameter("id", id)
fun String.withStartId(startId: Long) = this.withParameter("startId", startId)
fun String.withLimit(limit: Int) = this.withParameter("limit", limit)

fun mealInfo(): MealInfo {
    val meal = meal()
    return MealInfo(
        id = meal.id,
        name = meal.name,
        description = meal.description,
        price = meal.price,
        version = meal.version
    )
}

fun orderDetails(orderState: OrderState = OrderState.PAID) = order(state = orderState).toDetails()

class MockGetMenu(val mealInfo: MealInfo) : GetMenu {
    override fun execute() = listOf(mealInfo)
}

class MockAddMealToMenu : AddMealToMenu {

    lateinit var response: Either<AddMealToMenuUseCaseError, MealId>

    lateinit var name: MealName
    lateinit var description: MealDescription
    lateinit var price: Price

    override fun execute(
        name: MealName,
        description: MealDescription,
        price: Price,
    ): Either<AddMealToMenuUseCaseError, MealId> {
        this.name = name
        this.description = description
        this.price = price
        return response
    }

    fun verifyInvoked(
        name: MealName,
        description: MealDescription,
        price: Price,
    ) {
        name shouldBe this.name
        description shouldBe this.description
        price shouldBe this.price
    }
}

class MockGetMealById : GetMealById {

    lateinit var response: Either<GetMealByIdUseCaseError, MealInfo>
    lateinit var id: MealId

    override fun execute(id: MealId): Either<GetMealByIdUseCaseError, MealInfo> {
        this.id = id
        return response
    }

    fun verifyInvoked(id: MealId) {
        this.id shouldBe id
    }
}

class MockRemoveMealFromMenu : RemoveMealFromMenu {

    lateinit var response: Either<RemoveMealFromMenuUseCaseError, Unit>
    lateinit var id: MealId

    override fun execute(id: MealId): Either<RemoveMealFromMenuUseCaseError, Unit> {
        this.id = id
        return response
    }

    fun verifyInvoked(id: MealId) {
        this.id shouldBe id
    }
}

class MockGetOrderById : GetOrderById {

    lateinit var response: Either<GetOrderByIdUseCaseError, OrderDetails>
    lateinit var id: ShopOrderId

    override fun execute(id: ShopOrderId): Either<GetOrderByIdUseCaseError, OrderDetails> {
        this.id = id
        return response
    }

    fun verifyInvoked(id: ShopOrderId) {
        this.id shouldBe id
    }
}

class MockConfirmOrder : ConfirmOrder {

    lateinit var response: Either<ConfirmOrderUseCaseError, Unit>
    lateinit var id: ShopOrderId

    override fun execute(orderId: ShopOrderId): Either<ConfirmOrderUseCaseError, Unit> {
        this.id = orderId
        return response
    }

    fun verifyInvoked(id: ShopOrderId) {
        this.id shouldBe id
    }
}

class MockGetOrders : GetOrders {

    lateinit var response: Either<GetOrdersUseCaseError, List<OrderDetails>>

    lateinit var startId: ShopOrderId
    var limit: Int = Int.MIN_VALUE

    override fun execute(startId: ShopOrderId, limit: Int): Either<GetOrdersUseCaseError, List<OrderDetails>> {
        this.startId = startId
        this.limit = limit
        return response
    }

    fun verifyInvoked(startId: ShopOrderId, limit: Int) {
        this.startId shouldBe startId
        this.limit shouldBe limit
    }
}

class MockCancelOrder : CancelOrder {

    lateinit var response: Either<CancelOrderUseCaseError, Unit>
    lateinit var id: ShopOrderId

    override fun execute(orderId: ShopOrderId): Either<CancelOrderUseCaseError, Unit> {
        this.id = orderId
        return response
    }

    fun verifyInvoked(id: ShopOrderId) {
        this.id shouldBe id
    }
}