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

const val APPLICATION_HAL_JSON = "application/hal+json"

fun endpointUrl(suffix: String) = "http://localhost/rest/v1$suffix"

class MockGetMenu(val meal: Meal) : GetMenu {
    override fun execute() = listOf(MealInfo(id = meal.id,
        name = meal.name,
        description = meal.description,
        price = meal.price))
}

class MockAddMealToMenu : AddMealToMenu {
    override fun execute(
        name: MealName,
        description: MealDescription,
        price: Price,
    ): Either<AddMealToMenuUseCaseError, MealId> {
        TODO("Not yet implemented")
    }
}