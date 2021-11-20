package com.stringconcat.ddd.shop.persistence.postgresql

import javax.sql.DataSource
import liquibase.integration.spring.SpringLiquibase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.datasource.SingleConnectionDataSource
import org.testcontainers.containers.PostgreSQLContainer

@Configuration
class TestConfiguration {

    @Bean
    fun liquibase(dataSource: DataSource): SpringLiquibase {
        val liquibase = SpringLiquibase()
        liquibase.changeLog = "classpath:/com/stringconcat/ddd/shop/persistence/postgresql" +
                "/changelogs/main.xml"
        liquibase.dataSource = dataSource
        return liquibase
    }

    @Bean
    fun dataSource(container: PostgreSQLContainer<Nothing>) =
        SingleConnectionDataSource(
            container.jdbcUrl,
            container.username,
            container.password,
            true)

    @Bean(initMethod = "start")
    fun postgresqlContainer() = PostgreSQLContainer<Nothing>("postgres:14.1")

    @Bean
    fun jdbcTemplate(dataSource: DataSource) = NamedParameterJdbcTemplate(dataSource)
}