import arrow.core.Either
import com.stringconcat.ddd.shop.domain.meal
import com.stringconcat.ddd.shop.domain.menu.MealDescription
import com.stringconcat.ddd.shop.domain.menu.MealId
import com.stringconcat.ddd.shop.domain.menu.MealName
import com.stringconcat.ddd.shop.domain.menu.Price
import com.stringconcat.ddd.shop.usecase.menu.AddMealToMenu
import com.stringconcat.ddd.shop.usecase.menu.AddMealToMenuUseCaseError
import com.stringconcat.ddd.shop.usecase.menu.GetMealById
import com.stringconcat.ddd.shop.usecase.menu.GetMealByIdUseCaseError
import com.stringconcat.ddd.shop.usecase.menu.GetMenu
import com.stringconcat.ddd.shop.usecase.menu.RemoveMealFromMenu
import com.stringconcat.ddd.shop.usecase.menu.RemoveMealFromMenuUseCaseError
import com.stringconcat.ddd.shop.usecase.menu.dto.MealInfo
import io.kotest.matchers.shouldBe

const val APPLICATION_HAL_JSON = "application/hal+json"
const val API_V1_TYPE_BASE_URL = "http://localhost"

fun apiV1Url(suffix: String) = "http://localhost/rest/v1$suffix"
fun errorTypeUrl(suffix: String) = "$API_V1_TYPE_BASE_URL/$suffix"
fun notFoundTypeUrl() = errorTypeUrl("not_found")
fun badRequestTypeUrl() = errorTypeUrl("bad_request")

fun mealInfo(): MealInfo {
    val meal = meal()
    return MealInfo(id = meal.id,
        name = meal.name,
        description = meal.description,
        price = meal.price,
        version = meal.version)
}

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