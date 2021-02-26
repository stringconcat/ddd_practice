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

    fun execute(request: AddMealToMenuRequest): Either<AddMealToMenuUseCaseError, MealId> =
        tupled(
            MealName.from(request.name).mapLeft { it.toError() },
            MealDescription.from(request.description).mapLeft { it.toError() },
            Price.from(request.price).mapLeft { it.toError() }
        ).flatMap { params ->
            Meal.addMealToMenu(
                idGenerator = idGenerator,
                mealExistsRule = mealExistsRule,
                name = params.a,
                description = params.b,
                price = params.c
            ).mapLeft { e -> e.toError() }
        }.map { meal ->
            mealPersister.save(meal)
            meal.id
        }
}

data class AddMealToMenuRequest(val name: String, val description: String, val price: BigDecimal)

fun EmptyMealNameError.toError() = AddMealToMenuUseCaseError.InvalidName("Empty name")
fun EmptyDescriptionError.toError() = AddMealToMenuUseCaseError.InvalidDescription("Empty description")
fun AlreadyExistsWithSameNameError.toError() = AddMealToMenuUseCaseError.AlreadyExists

fun CreatePriceError.toError(): AddMealToMenuUseCaseError {
    return when (this) {
        is CreatePriceError.InvalidScale -> AddMealToMenuUseCaseError.InvalidPrice("Invalid scale")
        is CreatePriceError.NegativeValue -> AddMealToMenuUseCaseError.InvalidPrice("Negative value")
    }
}

// передавать сообщения из юзкейса не очень хорошо, лучше завести enum, но для примера нам сойдет
sealed class AddMealToMenuUseCaseError(open val message: String) {
    data class InvalidName(override val message: String) : AddMealToMenuUseCaseError(message)
    data class InvalidDescription(override val message: String) : AddMealToMenuUseCaseError(message)
    data class InvalidPrice(override val message: String) : AddMealToMenuUseCaseError(message)
    object AlreadyExists : AddMealToMenuUseCaseError("Meal already exists")
}