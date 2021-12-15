package com.stringconcat.ddd.performance

import io.gatling.javaapi.core.CoreDsl.constantUsersPerSec
import io.gatling.javaapi.core.CoreDsl.scenario
import io.gatling.javaapi.core.Simulation
import io.gatling.javaapi.http.HttpDsl.http

class GetMenuSimulation : Simulation() {

    private val httpProtocol = http
        .baseUrl("http://localhost:18081")

    private val scn = scenario("Get menu") // A scenario is a chain of requests and pauses
        .exec(http("Get menu request")
            .get("/"))

    init {
        setUp(scn.injectOpen(constantUsersPerSec(20.0)
            .during(15)).protocols(httpProtocol))
    }
}