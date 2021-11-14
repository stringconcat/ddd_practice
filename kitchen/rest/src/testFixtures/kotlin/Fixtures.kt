import arrow.core.Either
import com.stringconcat.ddd.kitchen.domain.order.KitchenOrderId
import com.stringconcat.ddd.kitchen.usecase.order.CookOrder
import com.stringconcat.ddd.kitchen.usecase.order.CookOrderUseCaseError
import io.kotest.matchers.shouldBe

const val API_V1_TYPE_BASE_URL = "http://localhost"

fun errorTypeUrl(suffix: String) = "$API_V1_TYPE_BASE_URL/$suffix"

fun notFoundTypeUrl() = errorTypeUrl("not_found")

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