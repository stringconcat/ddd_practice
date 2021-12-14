package com.stringconcat.ddd.shop.app.configuration

import com.stringconcat.ddd.shop.app.controllers.ShopErrorHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.hateoas.config.EnableHypermediaSupport
import org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableWebMvc
@EnableHypermediaSupport(type = [HypermediaType.HAL_FORMS])
class MvcConfiguration : WebMvcConfigurer {

    @Bean
    fun globalErrorHandler() = ShopErrorHandler()

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry
            .addResourceHandler("/webjars/**")
            .addResourceLocations("/webjars/")
    }
}