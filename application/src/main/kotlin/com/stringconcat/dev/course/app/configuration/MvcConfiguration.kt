package com.stringconcat.dev.course.app.configuration

import com.stringconcat.dev.course.app.controllers.GlobalErrorHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@Configuration
@EnableWebMvc
class MvcConfiguration {

    @Bean
    fun globalErrorHandler() = GlobalErrorHandler()
}