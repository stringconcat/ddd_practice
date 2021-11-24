package com.stringconcat.ddd.shop.crm

import au.com.dius.pact.consumer.MockServer
import au.com.dius.pact.consumer.dsl.PactDslWithProvider
import au.com.dius.pact.consumer.dsl.newJsonObject
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt
import au.com.dius.pact.consumer.junit5.PactTestFor
import au.com.dius.pact.core.model.PactSpecVersion
import au.com.dius.pact.core.model.annotations.Pact
import au.com.dius.pact.core.support.toUrl
import com.stringconcat.ddd.shop.domain.order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.http.MediaType

@ExtendWith(PactConsumerTestExt::class)
internal class CrmClientContractTest {

    private val order = order()
    private val headers = mapOf("Content-Type" to MediaType.APPLICATION_JSON_VALUE)

    @Pact(consumer = "CrmClient", provider = "CrmService")
    fun `contract - export order when it doesn't exist in crm`(builder: PactDslWithProvider) =
        builder.given("order doesn't exist")
            .uponReceiving("export order")
            .method("POST")
            .path("/orders")
            .headers(headers)
            .body(newJsonObject {
                numberType("id", order.id.value)
                stringType("customerId", order.forCustomer.value)
                decimalType("totalPrice", order.totalPrice().toBigDecimalValue())
            })
            .willRespondWith()
            .status(200)
            .body(newJsonObject {
                stringType("result", "SUCCESS")
            }).toPact()

    @Pact(consumer = "CrmClient", provider = "CrmService")
    fun `contract - export order when it exists in crm`(builder: PactDslWithProvider) =
        builder.given("order exists")
            .uponReceiving("export order")
            .method("POST")
            .path("/orders")
            .headers(headers)
            .willRespondWith()
            .status(200)
            .body(newJsonObject {
                stringType("result", "ALREADY_EXISTS")
            }).toPact()

    @Test
    @PactTestFor(pactMethod = "contract - export order when it doesn't exist in crm", pactVersion = PactSpecVersion.V3)
    fun `export order when it doesn't exist in crm`(mockServer: MockServer) {
        val client = CrmClient(mockServer.getUrl().toUrl()!!)
        client.exportOrder(order.id, order.forCustomer, order.totalPrice())
    }

    @Test
    @PactTestFor(pactMethod = "contract - export order when it exists in crm", pactVersion = PactSpecVersion.V3)
    fun `export order when it exists in crm`(mockServer: MockServer) {
        val client = CrmClient(mockServer.getUrl().toUrl()!!)
        client.exportOrder(order.id, order.forCustomer, order.totalPrice())
    }
}