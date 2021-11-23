package com.stringconcat.dev.course.app.configuration.shop

import com.stringconcat.ddd.shop.domain.cart.CartIdGenerator
import com.stringconcat.ddd.shop.domain.menu.MealAlreadyExists
import com.stringconcat.ddd.shop.domain.menu.MealIdGenerator
import com.stringconcat.ddd.shop.domain.order.CustomerHasActiveOrder
import com.stringconcat.ddd.shop.domain.order.MealPriceProvider
import com.stringconcat.ddd.shop.domain.order.ShopOrderIdGenerator
import com.stringconcat.ddd.shop.usecase.cart.access.CartExtractor
import com.stringconcat.ddd.shop.usecase.cart.access.CartPersister
import com.stringconcat.ddd.shop.usecase.cart.access.CartRemover
import com.stringconcat.ddd.shop.usecase.cart.rules.RemoveCartAfterCheckoutRule
import com.stringconcat.ddd.shop.usecase.cart.scenarios.AddMealToCartUseCase
import com.stringconcat.ddd.shop.usecase.cart.scenarios.GetCartUseCase
import com.stringconcat.ddd.shop.usecase.cart.scenarios.RemoveMealFromCartUseCase
import com.stringconcat.ddd.shop.usecase.menu.access.MealExtractor
import com.stringconcat.ddd.shop.usecase.menu.access.MealPersister
import com.stringconcat.ddd.shop.usecase.menu.invariants.MealAlreadyExistsImpl
import com.stringconcat.ddd.shop.usecase.menu.scenarios.AddMealToMenuUseCase
import com.stringconcat.ddd.shop.usecase.menu.scenarios.GetMealByIdUseCase
import com.stringconcat.ddd.shop.usecase.menu.scenarios.GetMenuUseCase
import com.stringconcat.ddd.shop.usecase.menu.scenarios.RemoveMealFromMenuUseCase
import com.stringconcat.ddd.shop.usecase.order.access.ShopOrderExtractor
import com.stringconcat.ddd.shop.usecase.order.access.ShopOrderPersister
import com.stringconcat.ddd.shop.usecase.order.invariants.CustomerHasActiveOrderImpl
import com.stringconcat.ddd.shop.usecase.order.providers.MealPriceProviderImpl
import com.stringconcat.ddd.shop.usecase.order.providers.OrderExporter
import com.stringconcat.ddd.shop.usecase.order.providers.PaymentUrlProvider
import com.stringconcat.ddd.shop.usecase.order.rules.ExportOrderAfterCheckoutRule
import com.stringconcat.ddd.shop.usecase.order.scenarios.CancelOrderUseCase
import com.stringconcat.ddd.shop.usecase.order.scenarios.CheckoutUseCase
import com.stringconcat.ddd.shop.usecase.order.scenarios.CompleteOrderUseCase
import com.stringconcat.ddd.shop.usecase.order.scenarios.ConfirmOrderUseCase
import com.stringconcat.ddd.shop.usecase.order.scenarios.GetLastOrderStateUseCase
import com.stringconcat.ddd.shop.usecase.order.scenarios.GetOrderByIdUseCase
import com.stringconcat.ddd.shop.usecase.order.scenarios.GetOrdersUseCase
import com.stringconcat.ddd.shop.usecase.order.scenarios.PayOrderHandler
import com.stringconcat.dev.course.app.event.EventPublisherImpl
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@Suppress("TooManyFunctions")
class ShopUseCaseConfiguration(
    @Value("\${paging.limit:10}")
    val limit: Int,
) {
    @Bean
    fun getMealByIdUseCase(mealExtractor: MealExtractor) =
        GetMealByIdUseCase(mealExtractor)

    @Bean
    fun addMealToCartUseCase(
        cartExtractor: CartExtractor,
        idGenerator: CartIdGenerator,
        mealExtractor: MealExtractor,
        cartPersister: CartPersister,
    ) = AddMealToCartUseCase(
        cartExtractor = cartExtractor,
        idGenerator = idGenerator,
        mealExtractor = mealExtractor,
        cartPersister = cartPersister
    )

    @Bean
    fun removeCartAfterCheckoutRule(
        cartExtractor: CartExtractor,
        cartRemover: CartRemover,
        domainEventPublisher: EventPublisherImpl,
    ): RemoveCartAfterCheckoutRule {
        val rule = RemoveCartAfterCheckoutRule(
            cartExtractor = cartExtractor,
            cartRemover = cartRemover
        )
        domainEventPublisher.registerListener(rule)
        return rule
    }

    @Bean
    fun removeMealFromCartUseCase(
        cartExtractor: CartExtractor,
        cartPersister: CartPersister,
    ) = RemoveMealFromCartUseCase(
        cartExtractor = cartExtractor,
        cartPersister = cartPersister
    )

    @Bean
    fun getCartUseCase(
        mealExtractor: MealExtractor,
        cartExtractor: CartExtractor,
    ) = GetCartUseCase(
        mealExtractor = mealExtractor,
        cartExtractor = cartExtractor
    )

    @Bean
    fun addMealToMenuUseCase(
        mealPersister: MealPersister,
        idGenerator: MealIdGenerator,
        mealExists: MealAlreadyExists,
    ) = AddMealToMenuUseCase(
        mealPersister = mealPersister,
        idGenerator = idGenerator,
        mealExists = mealExists
    )

    @Bean
    fun removeMealFromMenuUseCase(
        mealExtractor: MealExtractor,
        mealPersister: MealPersister,
    ) = RemoveMealFromMenuUseCase(
        mealExtractor = mealExtractor,
        mealPersister = mealPersister
    )

    @Bean
    fun payOrderHandler(
        shopOrderExtractor: ShopOrderExtractor,
        shopOrderPersister: ShopOrderPersister,
    ) = PayOrderHandler(
        shopOrderExtractor = shopOrderExtractor,
        shopOrderPersister = shopOrderPersister
    )

    @Bean
    fun cancelOrderUseCase(
        shopOrderExtractor: ShopOrderExtractor,
        shopOrderPersister: ShopOrderPersister,
    ) = CancelOrderUseCase(
        shopOrderExtractor = shopOrderExtractor,
        shopOrderPersister = shopOrderPersister
    )

    @Bean
    fun completeOrderUseCase(
        shopOrderExtractor: ShopOrderExtractor,
        shopOrderPersister: ShopOrderPersister,
    ) = CompleteOrderUseCase(
        shopOrderExtractor = shopOrderExtractor,
        shopOrderPersister = shopOrderPersister
    )

    @Bean
    fun checkoutUseCase(
        idGenerator: ShopOrderIdGenerator,
        cartExtractor: CartExtractor,
        activeOrder: CustomerHasActiveOrder,
        priceProvider: MealPriceProvider,
        paymentUrlProvider: PaymentUrlProvider,
        shopOrderPersister: ShopOrderPersister,
    ) = CheckoutUseCase(
        idGenerator = idGenerator,
        cartExtractor = cartExtractor,
        activeOrder = activeOrder,
        priceProvider = priceProvider,
        paymentUrlProvider = paymentUrlProvider,
        shopOrderPersister = shopOrderPersister
    )

    @Bean
    fun confirmOrderUseCase(
        shopOrderExtractor: ShopOrderExtractor,
        shopOrderPersister: ShopOrderPersister,
    ) = ConfirmOrderUseCase(
        shopOrderExtractor = shopOrderExtractor,
        shopOrderPersister = shopOrderPersister
    )

    @Bean
    fun getLastOrderStateUseCase(shopOrderExtractor: ShopOrderExtractor) =
        GetLastOrderStateUseCase(orderExtractor = shopOrderExtractor)

    @Bean
    fun getOrders(shopOrderExtractor: ShopOrderExtractor) =
        GetOrdersUseCase(orderExtractor = shopOrderExtractor) { limit }

    @Bean
    fun getOrderById(shopOrderExtractor: ShopOrderExtractor) =
        GetOrderByIdUseCase(shopOrderExtractor)

    @Bean
    fun getMenuUseCase(mealExtractor: MealExtractor) = GetMenuUseCase(mealExtractor)

    @Bean
    fun mealPriceProvider(mealExtractor: MealExtractor) = MealPriceProviderImpl(mealExtractor)

    @Bean
    fun shopHasActiveOrderRule(shopOrderExtractor: ShopOrderExtractor) =
        CustomerHasActiveOrderImpl(shopOrderExtractor)

    @Bean
    fun mealAlreadyExistsRule(mealExtractor: MealExtractor) = MealAlreadyExistsImpl(mealExtractor)

    @Bean
    fun exportOrderAfterCheckoutRule(
        orderExporter: OrderExporter,
        domainEventPublisher: EventPublisherImpl,
    ): ExportOrderAfterCheckoutRule {
        val rule = ExportOrderAfterCheckoutRule(orderExporter)
        domainEventPublisher.registerListener(rule)
        return rule
    }
}