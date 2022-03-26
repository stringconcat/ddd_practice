package com.stringconcat.ddd.shop.app.configuration

import com.stringconcat.ddd.shop.app.controllers.ShopErrorHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableWebMvc
class MvcConfiguration : WebMvcConfigurer {

    @Bean
    fun globalErrorHandler() = ShopErrorHandler()

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry
            .addResourceHandler("/webjars/**")
            .addResourceLocations("/webjars/")
    }
}