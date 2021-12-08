package com.stringconcat.dev.course.app.configuration

import com.stringconcat.dev.course.app.controllers.IndexEndpoint
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(
    KitchenContextConfiguration::class,
    SwaggerConfiguration::class,
    MvcConfiguration::class
)
@EnableAutoConfiguration
class ApplicationConfiguration {

    @Bean
    fun indexController() = IndexEndpoint()
}