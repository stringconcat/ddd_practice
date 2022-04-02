package com.stringconcat.ddd.performance

import com.github.javafaker.Faker
import com.stringconcat.ddd.tests.common.StandConfiguration
import com.stringconcat.ddd.tests.common.StandContainer
import io.gatling.javaapi.core.CoreDsl.constantUsersPerSec
import io.gatling.javaapi.core.CoreDsl.global
import io.gatling.javaapi.core.CoreDsl.scenario
import io.gatling.javaapi.core.Simulation
import io.gatling.javaapi.http.HttpDsl.http
import io.restassured.RestAssured
import io.restassured.http.ContentType
import java.math.BigDecimal
import java.net.URL
import java.util.UUID

@Suppress("MagicNumber")
class GetMenuSimulation : Simulation() {

    private val settings = StandConfiguration()
    private val simulationSettings = Settings()
    private val standContainer = StandContainer(settings)
    private val menuUrl: URL

    init {
        println("Simulation settings:\n$simulationSettings\n")
        standContainer.start()
        menuUrl = menuUrl()

        val httpProtocol = http
            .baseUrl(settings.shopBaseUrl)

        val scn = scenario("Get menu")
            .exec(http("Get menu request")
                .get(menuUrl.toString()))

        setUp(scn.injectOpen(constantUsersPerSec(simulationSettings.usersPerSec)
            .during(simulationSettings.duration.inWholeSeconds))
            .protocols(httpProtocol))
            .assertions(global().successfulRequests().percent().`is`(100.0))
            .assertions(global().responseTime().percentile4().lte(100))
    }

    override fun before() {
        val faker = Faker()
        repeat(simulationSettings.menuSize) {
            createMeal(name = "${faker.food().dish()}_${UUID.randomUUID()}",
                description = faker.food().ingredient(),
                price = BigDecimal.TEN)
        }
    }

    override fun after() {
        standContainer.stop()
    }

    private fun menuUrl() = URL("${settings.shopBaseUrl}/rest/shop/v1/menu")

    private fun createMeal(name: String, description: String, price: BigDecimal) {
        RestAssured.given()
            .body("""{ "name": "$name", "description": "$description", "price":"$price"}""")
            .contentType(ContentType.JSON)
            .post(menuUrl)
            .then()
            .statusCode(201)
    }
}