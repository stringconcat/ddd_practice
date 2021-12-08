package com.stringconcat.ddd.shop.app.listeners

class SendOrderToKitchenAfterConfirmationRuleTest {

//    @Test
//    fun `order successfully sent`() {
//
//        val meal = meal()
//        val price = price()
//        val count = count()
//        val order = order(orderItems = setOf(OrderItem(meal.id, price, count)))
//
//        val orderExtractor = MockShopOrderExtractor(order)
//
//        val mealExtractor = MockMealExtractor(meal)
//
//        val useCase = TestCreateOrder(Unit.right())
//
//        val rule = SendOrderToKitchenAfterConfirmationRule(
//            mealExtractor = mealExtractor,
//            shopOrderExtractor = orderExtractor,
//            createOrder = useCase
//        )
//
//        val event = ShopOrderConfirmedDomainEvent(order.id)
//        rule.handle(event)
//
//        useCase.request.id shouldBe order.id.toLongValue()
//        useCase.request.items shouldContainExactly listOf(
//            CreateOrderRequest.OrderItemData(
//                meal.name.toStringValue(),
//                count.toIntValue()
//            )
//        )
//        orderExtractor.verifyInvokedGetById(order.id)
//        mealExtractor.verifyInvokedGetById(meal.id)
//    }
//
//    @Test
//    fun `order not found`() {
//        val orderExtractor = MockShopOrderExtractor()
//
//        val mealExtractor = MockMealExtractor()
//
//        val useCase = TestCreateOrder(Unit.right())
//
//        val rule = SendOrderToKitchenAfterConfirmationRule(
//            mealExtractor = mealExtractor,
//            shopOrderExtractor = orderExtractor
//        )
//
//        val orderId = orderId()
//        val event = ShopOrderConfirmedDomainEvent(orderId)
//
//        shouldThrow<IllegalStateException> {
//            rule.handle(event)
//        }
//
//        orderExtractor.verifyInvokedGetById(orderId)
//        mealExtractor.verifyEmpty()
//        useCase.verifyZeroInteraction()
//    }
//
//    @Test
//    fun `meal not found`() {
//
//        val meal = meal()
//        val price = price()
//        val count = count()
//        val order = order(orderItems = setOf(OrderItem(meal.id, price, count)))
//
//        val orderExtractor = MockShopOrderExtractor(order)
//
//        val mealExtractor = MockMealExtractor()
//
//        val useCase = TestCreateOrder(Unit.right())
//
//        val rule = SendOrderToKitchenAfterConfirmationRule(
//            mealExtractor = mealExtractor,
//            shopOrderExtractor = orderExtractor,
//            createOrder = useCase
//        )
//
//        val event = ShopOrderConfirmedDomainEvent(order.id)
//
//        shouldThrow<IllegalStateException> {
//            rule.handle(event)
//        }
//
//        orderExtractor.verifyInvokedGetById(order.id)
//        mealExtractor.verifyInvokedGetById(meal.id)
//        useCase.verifyZeroInteraction()
//    }
//
//    @Test
//    fun `order creation error`() {
//
//        val meal = meal()
//        val price = price()
//        val count = count()
//        val order = order(orderItems = setOf(OrderItem(meal.id, price, count)))
//
//        val orderExtractor = MockShopOrderExtractor(order)
//
//        val mealExtractor = MockMealExtractor(meal)
//
//        val useCase = TestCreateOrder(CreateOrderUseCaseError.EmptyOrder.left())
//
//        val rule = SendOrderToKitchenAfterConfirmationRule(
//            mealExtractor = mealExtractor,
//            shopOrderExtractor = orderExtractor,
//            createOrder = useCase
//        )
//
//        val event = ShopOrderConfirmedDomainEvent(order.id)
//
//        shouldThrow<IllegalStateException> {
//            rule.handle(event)
//        }
//
//        useCase.request.id shouldBe order.id.toLongValue()
//        useCase.request.items shouldContainExactly listOf(
//            CreateOrderRequest.OrderItemData(
//                meal.name.toStringValue(),
//                count.toIntValue()
//            )
//        )
//        orderExtractor.verifyInvokedGetById(order.id)
//        mealExtractor.verifyInvokedGetById(meal.id)
//    }
//
//    private class TestCreateOrder(val response: Either<CreateOrderUseCaseError, Unit>) : CreateOrder {
//
//        lateinit var request: CreateOrderRequest
//
//        override fun execute(request: CreateOrderRequest): Either<CreateOrderUseCaseError, Unit> {
//            this.request = request
//            return response
//        }
//
//        fun verifyZeroInteraction() {
//            ::request.isInitialized.shouldBeFalse()
//        }
//    }
}