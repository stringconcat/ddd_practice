import arrow.core.Either
import com.stringconcat.ddd.kitchen.domain.order.KitchenOrderId
import com.stringconcat.ddd.kitchen.domain.order.order
import com.stringconcat.ddd.kitchen.usecase.order.CookOrder
import com.stringconcat.ddd.kitchen.usecase.order.CookOrderUseCaseError
import com.stringconcat.ddd.kitchen.usecase.order.GetOrderById
import com.stringconcat.ddd.kitchen.usecase.order.GetOrderByIdUseCaseError
import com.stringconcat.ddd.kitchen.usecase.order.GetOrders
import com.stringconcat.ddd.kitchen.usecase.order.dto.OrderDetails
import com.stringconcat.ddd.kitchen.usecase.order.dto.toDetails
import io.kotest.matchers.shouldBe

const val APPLICATION_HAL_JSON = "application/hal+json"
const val API_V1_TYPE_BASE_URL = "http://localhost"

fun errorTypeUrl(suffix: String) = "$API_V1_TYPE_BASE_URL/$suffix"
fun notFoundTypeUrl() = errorTypeUrl("not_found")

fun String.withHost() = API_V1_TYPE_BASE_URL.plus(this)
fun String.withParameter(name: String, value: Any) = this.replace("{$name}", value.toString())
fun String.withId(id: Long) = this.withParameter("id", id)

fun orderDetails() = order().toDetails()

class MockCookOrder : CookOrder {

    lateinit var response: Either<CookOrderUseCaseError, Unit>
    lateinit var id: KitchenOrderId

    override fun execute(orderId: KitchenOrderId): Either<CookOrderUseCaseError, Unit> {
        this.id = orderId
        return response
    }

    fun verifyInvoked(id: KitchenOrderId) {
        this.id shouldBe id
    }
}

class MockGetOrders : GetOrders {

    lateinit var response: List<OrderDetails>

    override fun execute(): List<OrderDetails> {
        return response
    }
}

class MockGetOrderById : GetOrderById {

    lateinit var response: Either<GetOrderByIdUseCaseError, OrderDetails>
    lateinit var id: KitchenOrderId

    override fun execute(id: KitchenOrderId): Either<GetOrderByIdUseCaseError, OrderDetails> {
        this.id = id
        return response
    }

    fun verifyInvoked(id: KitchenOrderId) {
        this.id shouldBe id
    }
}