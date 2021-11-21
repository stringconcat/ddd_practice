package com.stringconcat.dev.course.app.configuration

import com.stringconcat.dev.course.app.configuration.kitchen.KitchenPersistenceConfiguration
import com.stringconcat.dev.course.app.configuration.kitchen.KitchenRestConfiguration
import com.stringconcat.dev.course.app.configuration.kitchen.KitchenUseCaseConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(
    KitchenRestConfiguration::class,
    KitchenPersistenceConfiguration::class,
    KitchenUseCaseConfiguration::class
)
class KitchenContextConfiguration