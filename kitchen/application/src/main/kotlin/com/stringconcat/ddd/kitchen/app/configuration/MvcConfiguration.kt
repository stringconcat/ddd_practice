package com.stringconcat.ddd.kitchen.app.configuration

import com.stringconcat.ddd.kitchen.app.controllers.GlobalErrorHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@Configuration
@EnableWebMvc
class MvcConfiguration {

    @Bean
    fun globalErrorHandler() = GlobalErrorHandler()
}