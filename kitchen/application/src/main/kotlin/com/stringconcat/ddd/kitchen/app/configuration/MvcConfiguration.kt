package com.stringconcat.ddd.kitchen.app.configuration

import com.stringconcat.ddd.common.rest.GlobalErrorHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableWebMvc
class MvcConfiguration : WebMvcConfigurer {

    @Bean
    fun globalErrorHandler() = GlobalErrorHandler()

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry
                .addResourceHandler("/webjars/**")
                .addResourceLocations("/webjars/")
    }
}