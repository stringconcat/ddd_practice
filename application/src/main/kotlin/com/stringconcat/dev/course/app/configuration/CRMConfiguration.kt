package com.stringconcat.dev.course.app.configuration

import com.stringconcat.integration.crm.SimpleCRMConnector
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CRMConfiguration {

    @Bean
    fun simpleCRMConnector() = SimpleCRMConnector()
}
