package com.stringconcat.dev.course.app.configuration

import com.stringconcat.dev.course.app.configuration.shop.ShopIntegrationConfiguration
import com.stringconcat.dev.course.app.configuration.shop.ShopPersistenceConfiguration
import com.stringconcat.dev.course.app.configuration.shop.ShopRestConfiguration
import com.stringconcat.dev.course.app.configuration.shop.ShopTelnetConfiguration
import com.stringconcat.dev.course.app.configuration.shop.ShopUseCaseConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(
    ShopIntegrationConfiguration::class,
    ShopTelnetConfiguration::class,
    ShopRestConfiguration::class,
    ShopUseCaseConfiguration::class,
    ShopPersistenceConfiguration::class
)
class ShopContextConfiguration