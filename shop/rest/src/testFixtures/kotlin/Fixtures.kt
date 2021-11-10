import arrow.core.Either
import com.stringconcat.ddd.shop.domain.menu.Meal
import com.stringconcat.ddd.shop.domain.menu.MealDescription
import com.stringconcat.ddd.shop.domain.menu.MealId
import com.stringconcat.ddd.shop.domain.menu.MealName
import com.stringconcat.ddd.shop.domain.menu.Price
import com.stringconcat.ddd.shop.usecase.menu.AddMealToMenu
import com.stringconcat.ddd.shop.usecase.menu.AddMealToMenuUseCaseError
import com.stringconcat.ddd.shop.usecase.menu.GetMenu
import com.stringconcat.ddd.shop.usecase.menu.MealInfo
import io.kotest.matchers.shouldBe

const val APPLICATION_HAL_JSON = "application/hal+json"

fun endpointApiV1Url(suffix: String) = "http://localhost/rest/v1$suffix"
fun typeApiV1Url(suffix: String) = "http://localhost/$suffix"

class MockGetMenu(val meal: Meal) : GetMenu {
    override fun execute() = listOf(MealInfo(id = meal.id,
        name = meal.name,
        description = meal.description,
        price = meal.price))
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