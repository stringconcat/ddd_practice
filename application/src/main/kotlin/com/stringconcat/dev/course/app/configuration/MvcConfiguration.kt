package com.stringconcat.dev.course.app.configuration

import com.stringconcat.dev.course.app.controllers.GlobalErrorHandler
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@Configuration
@EnableWebMvc
class MvcConfiguration {

    @Value("server.contextPath")
    lateinit var contextPath: String

    @Bean
    fun globalErrorHandler() = GlobalErrorHandler()
}