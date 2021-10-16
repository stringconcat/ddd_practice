package com.stringconcat.dev.course.app

import com.stringconcat.dev.course.app.controllers.IndexController
import com.stringconcat.dev.course.app.controllers.URLs
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@WebMvcTest
@ContextConfiguration(classes = [MainTest.TestConfiguration::class])
class MainTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun `when call main page redirect be shown`() {
        mockMvc.get("/")
            .andExpect { status { is3xxRedirection() } }
            .andExpect { header { string("Location", URLs.rootMenu) } }
    }

    @Configuration
    class TestConfiguration {
        @Bean
        fun indexController() = IndexController()
    }
}
