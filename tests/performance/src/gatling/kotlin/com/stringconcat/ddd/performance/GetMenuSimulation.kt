package com.stringconcat.ddd.performance

import com.github.javafaker.Faker
import com.stringconcat.ddd.tests.common.StandConfiguration
import io.gatling.javaapi.core.CoreDsl.arrayFeeder
import io.gatling.javaapi.core.CoreDsl.constantUsersPerSec
import io.gatling.javaapi.core.CoreDsl.global
import io.gatling.javaapi.core.CoreDsl.scenario
import io.gatling.javaapi.core.Simulation
import io.gatling.javaapi.http.HttpDsl.http
import io.restassured.RestAssured
import io.restassured.http.ContentType
import java.math.BigDecimal
import java.net.URL
import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.containers.wait.strategy.Wait

@Suppress("MagicNumber")
class GetMenuSimulation : Simulation() {

    private val settings = StandConfiguration()
    private val simulationSettings = Settings()
    private val menuUrl: URL

    lateinit var dockerComposeContainer: DockerComposeContainer<Nothing>

    init {
        println("\nStand settings:\n$settings\n")
        println("Simulation settings:\n$simulationSettings\n")

        startDocker()
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

    private fun startDocker() {
        if (settings.startDocker) {
            dockerComposeContainer = DockerComposeContainer<Nothing>(settings.dockerCompose).apply {
                waitingFor("shop",
                    Wait.forLogMessage(".*Started ShopApplicationKt in.*", 1))
                waitingFor("kitchen",
                    Wait.forLogMessage(".*Started KitchenApplicationKt in.*", 1))
                withEnv(settings.dockerComposeEnv)
                start()
            }
        }
    }

    override fun before() {
        val faker = Faker()
        repeat(simulationSettings.menuSize) {
            createMeal(name = "${faker.food().dish()}_$it",
                description = faker.food().ingredient(),
                price = BigDecimal.TEN)
        }
    }

    override fun after() {
        if (settings.startDocker) {
            dockerComposeContainer.stop()
        }
    }

    private fun menuUrl() =
        URL(RestAssured.get(settings.shopBaseUrl)
            .jsonPath()
            .getString("_links.menu.href"))

    private fun createMeal(name: String, description: String, price: BigDecimal) {
        RestAssured.given()
            .body("""{ "name": "$name", "description": "$description", "price":"$price"}""")
            .contentType(ContentType.JSON)
            .post(menuUrl)
            .then()
            .statusCode(201)
    }
}