package com.stringconcat.ddd.order.usecase.menu

import arrow.core.Either
import arrow.core.extensions.either.apply.tupled
import arrow.core.flatMap
import com.stringconcat.ddd.order.domain.menu.AlreadyExistsWithSameNameError
import com.stringconcat.ddd.order.domain.menu.CreatePriceError
import com.stringconcat.ddd.order.domain.menu.EmptyDescriptionError
import com.stringconcat.ddd.order.domain.menu.EmptyMealNameError
import com.stringconcat.ddd.order.domain.menu.Meal
import com.stringconcat.ddd.order.domain.menu.MealDescription
import com.stringconcat.ddd.order.domain.menu.MealId
import com.stringconcat.ddd.order.domain.menu.MealIdGenerator
import com.stringconcat.ddd.order.domain.menu.MealName
import com.stringconcat.ddd.order.domain.menu.Price
import com.stringconcat.ddd.order.domain.rules.MealAlreadyExistsRule
import java.math.BigDecimal

class AddMealToMenuUseCase(
    private val mealPersister: MealPersister,
    private val idGenerator: MealIdGenerator,
    private val mealExistsRule: MealAlreadyExistsRule
) {

    fun addMealToMenu(request: AddMealToMenuRequest): Either<AddMealToMenuUseCaseError, MealId> =
        tupled(
            MealName.from(request.name),
            MealDescription.from(request.description),
            Price.from(request.price)
        ).flatMap {
            Meal.addMealToMenu(
                idGenerator = idGenerator,
                mealExistsRule = mealExistsRule,
                name = it.a,
                description = it.b,
                price = it.c
            )
        }.map {
            mealPersister.save(it);
            it.id
        }.mapLeft {
            it.toError();
        }
}

data class AddMealToMenuRequest(val name: String, val description: String, val price: BigDecimal)

fun BusinessError.toError() = AddMealToMenuUseCaseError.SOmethingWentWrong("whoops")
fun EmptyMealNameError.toError() = AddMealToMenuUseCaseError.InvalidName("Empty name")
fun EmptyDescriptionError.toError() = AddMealToMenuUseCaseError.InvalidDescription("Empty description")
fun AlreadyExistsWithSameNameError.toError() = AddMealToMenuUseCaseError.AlreadyExists
fun CreatePriceError.InvalidScale.toError() = AddMealToMenuUseCaseError.InvalidPrice("Invalid scale")
fun CreatePriceError.NegativeValue.toError() = AddMealToMenuUseCaseError.InvalidPrice("Negative value")

// передавать сообщения из юзкейса не очень хорошо, лучше завести enum, но для примера нам сойдет
sealed class AddMealToMenuUseCaseError(val message: String) {
    class InvalidName(message: String) : AddMealToMenuUseCaseError(message)
    class InvalidDescription(message: String) : AddMealToMenuUseCaseError(message)
    class InvalidPrice(message: String) : AddMealToMenuUseCaseError(message)
    object AlreadyExists : AddMealToMenuUseCaseError("Already exists")
}