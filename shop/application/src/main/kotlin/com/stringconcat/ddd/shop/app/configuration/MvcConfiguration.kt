package com.stringconcat.ddd.shop.app.configuration

import com.stringconcat.ddd.shop.app.controllers.ShopErrorHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@Configuration
@EnableWebMvc
class MvcConfiguration {

    @Bean
    fun globalErrorHandler() = ShopErrorHandler()
}