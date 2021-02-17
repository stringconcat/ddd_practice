package com.stringconcat.ddd.order.usecase.menu

import arrow.core.Either
import arrow.core.left
import arrow.core.right
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

    @Suppress("ReturnCount")
    fun addMealToMenu(request: AddMealToMenuRequest): Either<AddMealToMenuUseCaseError, MealId> {

        val name = MealName.from(request.name)
            .fold(
                ifLeft = { return@addMealToMenu it.toError().left() },
                ifRight = { it })

        val description = MealDescription.from(request.description)
            .fold(
                ifLeft = { return@addMealToMenu it.toError().left() },
                ifRight = { it })

        val price = Price.from(request.price)
            .fold(
                ifLeft = { return@addMealToMenu it.toError().left() },
                ifRight = { it })

        return Meal.addMealToMenu(
            idGenerator = idGenerator,
            mealExistsRule = mealExistsRule,
            name = name,
            description = description,
            price = price
        ).fold(
            ifLeft = { it.toError().left() },
            ifRight = {
                mealPersister.save(it)
                it.id.right()
            })
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
sealed class AddMealToMenuUseCaseError(val message: String) {
    class InvalidName(message: String) : AddMealToMenuUseCaseError(message)
    class InvalidDescription(message: String) : AddMealToMenuUseCaseError(message)
    class InvalidPrice(message: String) : AddMealToMenuUseCaseError(message)
    object AlreadyExists : AddMealToMenuUseCaseError("Already exists")
}