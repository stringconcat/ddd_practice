package com.stringconcat.dev.course.app.configuration

import com.stringconcat.ddd.common.types.base.EventPublisher
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
import com.stringconcat.ddd.shop.telnet.cart.AddMealToCartCommand
import com.stringconcat.ddd.shop.telnet.cart.CheckoutCommand
import com.stringconcat.ddd.shop.telnet.cart.GetCartCommand
import com.stringconcat.ddd.shop.telnet.cart.RemoveMealFromCartCommand
import com.stringconcat.ddd.shop.telnet.menu.GetMenuCommand
import com.stringconcat.ddd.shop.telnet.order.GetLastOrderStateCommand
import com.stringconcat.ddd.shop.usecase.cart.AddMealToCartUseCase
import com.stringconcat.ddd.shop.usecase.cart.CartExtractor
import com.stringconcat.ddd.shop.usecase.cart.CartPersister
import com.stringconcat.ddd.shop.usecase.cart.CartRemover
import com.stringconcat.ddd.shop.usecase.cart.GetCart
import com.stringconcat.ddd.shop.usecase.cart.GetCartUseCase
import com.stringconcat.ddd.shop.usecase.cart.RemoveCartHandler
import com.stringconcat.ddd.shop.usecase.cart.RemoveMealFromCart
import com.stringconcat.ddd.shop.usecase.cart.RemoveMealFromCartUseCase
import com.stringconcat.ddd.shop.usecase.menu.AddMealToMenu
import com.stringconcat.ddd.shop.usecase.menu.scenarios.AddMealToMenuUseCase
import com.stringconcat.ddd.shop.usecase.menu.GetMenu
import com.stringconcat.ddd.shop.usecase.menu.scenarios.GetMenuUseCase
import com.stringconcat.ddd.shop.usecase.menu.scenarios.MealExtractor
import com.stringconcat.ddd.shop.usecase.menu.scenarios.MealPersister
import com.stringconcat.ddd.shop.usecase.menu.RemoveMealFromMenu
import com.stringconcat.ddd.shop.usecase.menu.scenarios.RemoveMealFromMenuUseCase
import com.stringconcat.ddd.shop.usecase.order.CancelOrder
import com.stringconcat.ddd.shop.usecase.order.CancelOrderUseCase
import com.stringconcat.ddd.shop.usecase.order.Checkout
import com.stringconcat.ddd.shop.usecase.order.CheckoutUseCase
import com.stringconcat.ddd.shop.usecase.order.CompleteOrderUseCase
import com.stringconcat.ddd.shop.usecase.order.ConfirmOrder
import com.stringconcat.ddd.shop.usecase.order.ConfirmOrderUseCase
import com.stringconcat.ddd.shop.usecase.order.GetLastOrderState
import com.stringconcat.ddd.shop.usecase.order.GetLastOrderStateUseCase
import com.stringconcat.ddd.shop.usecase.order.GetOrders
import com.stringconcat.ddd.shop.usecase.order.GetOrdersUseCase
import com.stringconcat.ddd.shop.usecase.order.PayOrderHandler
import com.stringconcat.ddd.shop.usecase.order.PaymentUrlProvider
import com.stringconcat.ddd.shop.usecase.order.ShopOrderExtractor
import com.stringconcat.ddd.shop.usecase.order.ShopOrderPersister
import com.stringconcat.ddd.shop.usecase.providers.MealPriceProviderImpl
import com.stringconcat.ddd.shop.usecase.rules.CustomerHasActiveOrderImpl
import com.stringconcat.ddd.shop.usecase.rules.MealAlreadyExistsImpl
import com.stringconcat.ddd.shop.web.menu.MenuController
import com.stringconcat.ddd.shop.web.order.ShopOrderController
import com.stringconcat.dev.course.app.event.EventPublisherImpl
import com.stringconcat.dev.course.app.listeners.RemoveCartAfterCheckoutRule
import com.stringconcat.shop.payment.SimplePaymentUrlProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.net.URL

@Suppress("TooManyFunctions")
@Configuration
class ShopContextConfiguration {

    @Bean
    fun cartRepository(eventPublisher: EventPublisher) = InMemoryCartRepository(eventPublisher)

    @Bean
    fun mealRepository(eventPublisher: EventPublisher) = InMemoryMealRepository(eventPublisher)

    @Bean
    fun shopOrderRepository(eventPublisher: EventPublisher) = InMemoryShopOrderRepository(eventPublisher)

    @Bean
    fun cartIdGenerator() = InMemoryIncrementalCartIdGenerator()

    @Bean
    fun mealIdGenerator() = InMemoryIncrementalMealIdGenerator()

    @Bean
    fun shopOrderIdGenerator() = InMemoryIncrementalShopOrderIdGenerator()

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
    fun getCartUseCase(
        mealExtractor: MealExtractor,
        cartExtractor: CartExtractor
    ) = GetCartUseCase(
        mealExtractor = mealExtractor,
        cartExtractor = cartExtractor
    )

    @Bean
    fun addMealToMenuUseCase(
        mealPersister: MealPersister,
        idGenerator: MealIdGenerator,
        mealExists: MealAlreadyExists
    ) = AddMealToMenuUseCase(
        mealPersister = mealPersister,
        idGenerator = idGenerator,
        mealExists = mealExists
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
        shopOrderExtractor: ShopOrderExtractor,
        shopOrderPersister: ShopOrderPersister
    ) = PayOrderHandler(
        shopOrderExtractor = shopOrderExtractor,
        shopOrderPersister = shopOrderPersister
    )

    @Bean
    fun cancelOrderUseCase(
        shopOrderExtractor: ShopOrderExtractor,
        shopOrderPersister: ShopOrderPersister
    ) = CancelOrderUseCase(
        shopOrderExtractor = shopOrderExtractor,
        shopOrderPersister = shopOrderPersister
    )

    @Bean
    fun completeOrderUseCase(
        shopOrderExtractor: ShopOrderExtractor,
        shopOrderPersister: ShopOrderPersister
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
        shopOrderPersister: ShopOrderPersister
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
        shopOrderPersister: ShopOrderPersister
    ) = ConfirmOrderUseCase(
        shopOrderExtractor = shopOrderExtractor,
        shopOrderPersister = shopOrderPersister
    )

    @Bean
    fun getLastOrderStateUseCase(shopOrderExtractor: ShopOrderExtractor) =
        GetLastOrderStateUseCase(orderExtractor = shopOrderExtractor)

    @Bean
    fun getOrders(shopOrderExtractor: ShopOrderExtractor) =
        GetOrdersUseCase(orderExtractor = shopOrderExtractor)

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
    fun checkoutListener(
        removeCartHandler: RemoveCartHandler,
        domainEventPublisher: EventPublisherImpl
    ): RemoveCartAfterCheckoutRule {
        val listener = RemoveCartAfterCheckoutRule(removeCartHandler)
        domainEventPublisher.registerListener(listener)
        return listener
    }

    @Bean
    fun menuController(addMealToMenu: AddMealToMenu, removeMealFromMenu: RemoveMealFromMenu, getMenu: GetMenu) =
        MenuController(addMealToMenu, removeMealFromMenu, getMenu)

    @Bean
    fun shopOrderController(getOrders: GetOrders, confirmOrder: ConfirmOrder, cancelOrder: CancelOrder) =
        ShopOrderController(getOrders, confirmOrder, cancelOrder)

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
}