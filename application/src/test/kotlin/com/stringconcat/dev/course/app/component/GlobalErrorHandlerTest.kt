package com.stringconcat.dev.course.app.component

import com.stringconcat.dev.course.app.ShopComponentTestConfiguration
import com.stringconcat.dev.course.app.TEST_TELNET_PORT
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus

@SpringBootTest(classes = [ShopComponentTestConfiguration::class],
    properties = ["telnet.port=$TEST_TELNET_PORT"],
    webEnvironment = WebEnvironment.RANDOM_PORT)
class GlobalErrorHandlerTest(@LocalServerPort val port: Int) {

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Test
    fun `illegal state exception must be converted to HTTP 500`() {
        val result = restTemplate.getForEntity("/illegalState", String::class.java)
        result.statusCode shouldBe HttpStatus.INTERNAL_SERVER_ERROR
    }

    @Test
    fun `storage conflict state exception must be converted to HTTP 409`() {
        val result = restTemplate.getForEntity("/storageConflict", String::class.java)
        result.statusCode shouldBe HttpStatus.CONFLICT
    }
}