package com.stringconcat.dev.course.app.component

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [ShopComponentTestConfiguration::class], properties = ["telnet.port=22121"])
class CheckoutComponentTest {
    @Test
    fun `empty test`() {
        println("it works")
    }
}