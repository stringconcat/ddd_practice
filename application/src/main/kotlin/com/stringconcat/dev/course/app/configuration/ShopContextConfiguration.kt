package com.stringconcat.dev.course.app.configuration

import com.stringconcat.ddd.common.events.DomainEventPublisher
import com.stringconcat.ddd.shop.domain.cart.CartIdGenerator
import com.stringconcat.ddd.shop.domain.menu.MealAlreadyExists
import com.stringconcat.ddd.shop.domain.menu.MealIdGenerator
import com.stringconcat.ddd.shop.domain.order.CustomerHasActiveOrder
import com.stringconcat.ddd.shop.domain.order.MealPriceProvider
import com.stringconcat.ddd.shop.domain.order.ShopOrderIdGenerator
import com.stringconcat.ddd.shop.persistence.cart.InMemoryCartRepository
import com.stringconcat.ddd.shop.persistence.cart.InMemoryIncrementalCartIdGenerator
import com.stringconcat.ddd.shop.persistence.menu.InMemoryIncrementalMealIdGenerator
import com.stringconcat.ddd.shop.persistence.menu.InMemoryMealRepository
import com.stringconcat.ddd.shop.persistence.order.InMemoryIncrementalShopOrderIdGenerator
import com.stringconcat.ddd.shop.persistence.order.InMemoryShopOrderRepository
import com.stringconcat.ddd.shop.rest.menu.AddMealToMenuEndpoint
import com.stringconcat.ddd.shop.rest.menu.GetMealByIdEndpoint
import com.stringconcat.ddd.shop.rest.menu.GetMenuEndpoint
import com.stringconcat.ddd.shop.rest.menu.RemoveMealFromMenuEndpoint
import com.stringconcat.ddd.shop.rest.order.CancelOrderEndpoint
import com.stringconcat.ddd.shop.rest.order.ConfirmOrderEndpoint
import com.stringconcat.ddd.shop.rest.order.GetOrderByIdEndpoint
import com.stringconcat.ddd.shop.telnet.cart.AddMealToCartCommand
import com.stringconcat.ddd.shop.telnet.cart.CheckoutCommand
import com.stringconcat.ddd.shop.telnet.cart.GetCartCommand
import com.stringconcat.ddd.shop.telnet.cart.RemoveMealFromCartCommand
import com.stringconcat.ddd.shop.telnet.menu.GetMenuCommand
import com.stringconcat.ddd.shop.telnet.order.GetLastOrderStateCommand
import com.stringconcat.ddd.shop.usecase.cart.GetCart
import com.stringconcat.ddd.shop.usecase.cart.RemoveMealFromCart
import com.stringconcat.ddd.shop.usecase.cart.access.CartExtractor
import com.stringconcat.ddd.shop.usecase.cart.access.CartPersister
import com.stringconcat.ddd.shop.usecase.cart.access.CartRemover
import com.stringconcat.ddd.shop.usecase.cart.rules.RemoveCartAfterCheckoutRule
import com.stringconcat.ddd.shop.usecase.cart.scenarios.AddMealToCartUseCase
import com.stringconcat.ddd.shop.usecase.cart.scenarios.GetCartUseCase
import com.stringconcat.ddd.shop.usecase.cart.scenarios.RemoveMealFromCartUseCase
import com.stringconcat.ddd.shop.usecase.menu.AddMealToMenu
import com.stringconcat.ddd.shop.usecase.menu.GetMealById
import com.stringconcat.ddd.shop.usecase.menu.GetMenu
import com.stringconcat.ddd.shop.usecase.menu.RemoveMealFromMenu
import com.stringconcat.ddd.shop.usecase.menu.access.MealExtractor
import com.stringconcat.ddd.shop.usecase.menu.access.MealPersister
import com.stringconcat.ddd.shop.usecase.menu.invariants.MealAlreadyExistsImpl
import com.stringconcat.ddd.shop.usecase.menu.scenarios.AddMealToMenuUseCase
import com.stringconcat.ddd.shop.usecase.menu.scenarios.GetMealByIdUseCase
import com.stringconcat.ddd.shop.usecase.menu.scenarios.GetMenuUseCase
import com.stringconcat.ddd.shop.usecase.menu.scenarios.RemoveMealFromMenuUseCase
import com.stringconcat.ddd.shop.usecase.order.CancelOrder
import com.stringconcat.ddd.shop.usecase.order.Checkout
import com.stringconcat.ddd.shop.usecase.order.ConfirmOrder
import com.stringconcat.ddd.shop.usecase.order.GetLastOrderState
import com.stringconcat.ddd.shop.usecase.order.GetOrderById
import com.stringconcat.ddd.shop.usecase.order.access.ShopOrderExtractor
import com.stringconcat.ddd.shop.usecase.order.access.ShopOrderPersister
import com.stringconcat.ddd.shop.usecase.order.invariants.CustomerHasActiveOrderImpl
import com.stringconcat.ddd.shop.usecase.order.providers.MealPriceProviderImpl
import com.stringconcat.ddd.shop.usecase.order.providers.PaymentUrlProvider
import com.stringconcat.ddd.shop.usecase.order.scenarios.CancelOrderUseCase
import com.stringconcat.ddd.shop.usecase.order.scenarios.CheckoutUseCase
import com.stringconcat.ddd.shop.usecase.order.scenarios.CompleteOrderUseCase
import com.stringconcat.ddd.shop.usecase.order.scenarios.ConfirmOrderUseCase
import com.stringconcat.ddd.shop.usecase.order.scenarios.GetLastOrderStateUseCase
import com.stringconcat.ddd.shop.usecase.order.scenarios.GetOrderByIdUseCase
import com.stringconcat.ddd.shop.usecase.order.scenarios.GetOrdersUseCase
import com.stringconcat.ddd.shop.usecase.order.scenarios.PayOrderHandler
import com.stringconcat.ddd.shop.web.menu.MenuController
import com.stringconcat.dev.course.app.event.EventPublisherImpl
import com.stringconcat.shop.payment.SimplePaymentUrlProvider
import java.net.URL
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Suppress("TooManyFunctions")
@Configuration
class ShopContextConfiguration(
    @Value("\${paging.limit:10}")
    val limit: Int,
) {

    @Bean
    fun cartRepository(eventPublisher: DomainEventPublisher) = InMemoryCartRepository(eventPublisher)

    @Bean
    fun mealRepository(eventPublisher: DomainEventPublisher) = InMemoryMealRepository(eventPublisher)

    @Bean
    fun shopOrderRepository(eventPublisher: DomainEventPublisher) = InMemoryShopOrderRepository(eventPublisher)

    @Bean
    fun cartIdGenerator() = InMemoryIncrementalCartIdGenerator()

    @Bean
    fun mealIdGenerator() = InMemoryIncrementalMealIdGenerator()

    @Bean
    fun shopOrderIdGenerator() = InMemoryIncrementalShopOrderIdGenerator()

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
    fun paymentUrlProvider() = SimplePaymentUrlProvider(URL("http://localhost:8080"))

    @Bean
    fun menuController(removeMealFromMenu: RemoveMealFromMenu, getMenu: GetMenu) =
        MenuController(removeMealFromMenu, getMenu)

    @Bean
    fun getMenuCommand(useCase: GetMenu) = GetMenuCommand(useCase)

    @Bean
    fun addMealToCartCommand(useCase: AddMealToCartUseCase) = AddMealToCartCommand(useCase)

    @Bean
    fun removeMealFromCartCommand(useCase: RemoveMealFromCart) = RemoveMealFromCartCommand(useCase)

    @Bean
    fun getCartCommand(useCase: GetCart) = GetCartCommand(useCase)

    @Bean
    fun checkoutCommand(useCase: Checkout) = CheckoutCommand(useCase)

    @Bean
    fun getLastOrderStateCommand(useCase: GetLastOrderState) = GetLastOrderStateCommand(useCase)

    @Bean
    fun getMenuEndpoint(getMenu: GetMenu) = GetMenuEndpoint(getMenu)

    @Bean
    fun addMealToMenuEndpoint(addMealToMenu: AddMealToMenu) = AddMealToMenuEndpoint(addMealToMenu)

    @Bean
    fun removeMealFromMenuEndpoint(removeMealFromMenu: RemoveMealFromMenu) =
        RemoveMealFromMenuEndpoint(removeMealFromMenu)

    @Bean
    fun getMenuEndpoint(getMealById: GetMealById) = GetMealByIdEndpoint(getMealById)

    @Bean
    fun getOrderByIdEndpoint(getOrderById: GetOrderById) = GetOrderByIdEndpoint(getOrderById)

    @Bean
    fun confirmOrderEndpoint(confirmOrder: ConfirmOrder) = ConfirmOrderEndpoint(confirmOrder)

    @Bean
    fun cancelOrderEndpoint(cancelOrder: CancelOrder) = CancelOrderEndpoint(cancelOrder)
}