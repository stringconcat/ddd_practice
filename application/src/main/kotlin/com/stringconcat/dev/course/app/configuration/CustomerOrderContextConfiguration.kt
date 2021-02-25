package com.stringconcat.dev.course.app.configuration

import com.stringconcat.ddd.common.types.base.EventPublisher
import com.stringconcat.ddd.order.domain.cart.CartIdGenerator
import com.stringconcat.ddd.order.domain.menu.MealIdGenerator
import com.stringconcat.ddd.order.domain.order.CustomerOrderIdGenerator
import com.stringconcat.ddd.order.domain.providers.MealPriceProvider
import com.stringconcat.ddd.order.domain.rules.CustomerHasActiveOrderRule
import com.stringconcat.ddd.order.domain.rules.MealAlreadyExistsRule
import com.stringconcat.ddd.order.persistence.cart.InMemoryCartRepository
import com.stringconcat.ddd.order.persistence.cart.InMemoryIncrementalCartIdGenerator
import com.stringconcat.ddd.order.persistence.menu.InMemoryIncrementalMealIdGenerator
import com.stringconcat.ddd.order.persistence.menu.InMemoryMealRepository
import com.stringconcat.ddd.order.persistence.order.InMemoryCustomerOrderRepository
import com.stringconcat.ddd.order.persistence.order.InMemoryIncrementalCustomerOrderIdGenerator
import com.stringconcat.ddd.order.usecase.cart.AddMealToCartUseCase
import com.stringconcat.ddd.order.usecase.cart.CartExtractor
import com.stringconcat.ddd.order.usecase.cart.CartPersister
import com.stringconcat.ddd.order.usecase.cart.CartRemover
import com.stringconcat.ddd.order.usecase.cart.RemoveCartHandler
import com.stringconcat.ddd.order.usecase.cart.RemoveMealFromCartUseCase
import com.stringconcat.ddd.order.usecase.menu.AddMealToMenuUseCase
import com.stringconcat.ddd.order.usecase.menu.GetMenuUseCase
import com.stringconcat.ddd.order.usecase.menu.MealExtractor
import com.stringconcat.ddd.order.usecase.menu.MealPersister
import com.stringconcat.ddd.order.usecase.menu.RemoveMealFromMenuUseCase
import com.stringconcat.ddd.order.usecase.order.CancelOrderUseCase
import com.stringconcat.ddd.order.usecase.order.CheckoutUseCase
import com.stringconcat.ddd.order.usecase.order.CompleteOrderUseCase
import com.stringconcat.ddd.order.usecase.order.ConfirmOrderUseCase
import com.stringconcat.ddd.order.usecase.order.CustomerOrderExtractor
import com.stringconcat.ddd.order.usecase.order.CustomerOrderPersister
import com.stringconcat.ddd.order.usecase.order.PayOrderHandler
import com.stringconcat.ddd.order.usecase.order.PaymentUrlProvider
import com.stringconcat.ddd.order.usecase.providers.MealPriceProviderImpl
import com.stringconcat.ddd.order.usecase.rules.CustomerHasActiveOrderRuleImpl
import com.stringconcat.ddd.order.usecase.rules.MealAlreadyExistsRuleImpl
import com.stringconcat.dev.course.app.event.EventPublisherImpl
import com.stringconcat.dev.course.app.listeners.CheckoutListener
import com.stringconcat.integration.payment.SimplePaymentUrlProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Suppress("TooManyFunctions")
@Configuration
class CustomerOrderContextConfiguration {

    @Bean
    fun cartRepository(eventPublisher: EventPublisher) = InMemoryCartRepository(eventPublisher)

    @Bean
    fun mealRepository(eventPublisher: EventPublisher) = InMemoryMealRepository(eventPublisher)

    @Bean
    fun customerOrderRepository(eventPublisher: EventPublisher) = InMemoryCustomerOrderRepository(eventPublisher)

    @Bean
    fun cartIdGenerator() = InMemoryIncrementalCartIdGenerator()

    @Bean
    fun mealIdGenerator() = InMemoryIncrementalMealIdGenerator()

    @Bean
    fun customerOrderIdGenerator() = InMemoryIncrementalCustomerOrderIdGenerator()

    @Bean
    fun addMealToCartUseCase(
        cartExtractor: CartExtractor,
        idGenerator: CartIdGenerator,
        mealExtractor: MealExtractor,
        cartPersister: CartPersister
    ) = AddMealToCartUseCase(
        cartExtractor = cartExtractor,
        idGenerator = idGenerator,
        mealExtractor = mealExtractor,
        cartPersister = cartPersister
    )

    @Bean
    fun removeCartHandler(
        cartExtractor: CartExtractor,
        cartRemover: CartRemover
    ) = RemoveCartHandler(
        cartExtractor = cartExtractor,
        cartRemover = cartRemover
    )

    @Bean
    fun removeMealFromCartUseCase(
        cartExtractor: CartExtractor,
        cartPersister: CartPersister
    ) = RemoveMealFromCartUseCase(
        cartExtractor = cartExtractor,
        cartPersister = cartPersister
    )

    @Bean
    fun addMealToMenuUseCase(
        mealPersister: MealPersister,
        idGenerator: MealIdGenerator,
        mealExistsRule: MealAlreadyExistsRule
    ) = AddMealToMenuUseCase(
        mealPersister = mealPersister,
        idGenerator = idGenerator,
        mealExistsRule = mealExistsRule
    )

    @Bean
    fun removeMealFromMenuUseCase(
        mealExtractor: MealExtractor,
        mealPersister: MealPersister
    ) = RemoveMealFromMenuUseCase(
        mealExtractor = mealExtractor,
        mealPersister = mealPersister
    )

    @Bean
    fun payOrderHandler(
        customerOrderExtractor: CustomerOrderExtractor,
        customerOrderPersister: CustomerOrderPersister
    ) = PayOrderHandler(
        customerOrderExtractor = customerOrderExtractor,
        customerOrderPersister = customerOrderPersister
    )

    @Bean
    fun cancelOrderUseCase(
        customerOrderExtractor: CustomerOrderExtractor,
        customerOrderPersister: CustomerOrderPersister
    ) = CancelOrderUseCase(
        customerOrderExtractor = customerOrderExtractor,
        customerOrderPersister = customerOrderPersister
    )

    @Bean
    fun completeOrderUseCase(
        customerOrderExtractor: CustomerOrderExtractor,
        customerOrderPersister: CustomerOrderPersister
    ) = CompleteOrderUseCase(
        customerOrderExtractor = customerOrderExtractor,
        customerOrderPersister = customerOrderPersister
    )

    @Bean
    fun checkoutUseCase(
        idGenerator: CustomerOrderIdGenerator,
        cartExtractor: CartExtractor,
        activeOrderRule: CustomerHasActiveOrderRule,
        priceProvider: MealPriceProvider,
        paymentUrlProvider: PaymentUrlProvider,
        customerOrderPersister: CustomerOrderPersister
    ) = CheckoutUseCase(
        idGenerator = idGenerator,
        cartExtractor = cartExtractor,
        activeOrderRule = activeOrderRule,
        priceProvider = priceProvider,
        paymentUrlProvider = paymentUrlProvider,
        customerOrderPersister = customerOrderPersister
    )

    @Bean
    fun confirmOrderUseCase(
        customerOrderExtractor: CustomerOrderExtractor,
        customerOrderPersister: CustomerOrderPersister
    ) = ConfirmOrderUseCase(
        customerOrderExtractor = customerOrderExtractor,
        customerOrderPersister = customerOrderPersister
    )

    @Bean
    fun getMenuUseCase(mealExtractor: MealExtractor) = GetMenuUseCase(mealExtractor)

    @Bean
    fun mealPriceProvider(mealExtractor: MealExtractor) = MealPriceProviderImpl(mealExtractor)

    @Bean
    fun customerHasActiveOrderRule(customerOrderExtractor: CustomerOrderExtractor) =
        CustomerHasActiveOrderRuleImpl(customerOrderExtractor)

    @Bean
    fun mealAlreadyExistsRule(mealExtractor: MealExtractor) = MealAlreadyExistsRuleImpl(mealExtractor)

    @Bean
    fun paymentUrlProvider() = SimplePaymentUrlProvider()

    @Bean
    fun checkoutListener(
        removeCartHandler: RemoveCartHandler,
        domainEventPublisher: EventPublisherImpl
    ): CheckoutListener {
        val listener = CheckoutListener(removeCartHandler)
        domainEventPublisher.registerListener(listener)
        return listener
    }
}