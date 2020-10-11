package ro.contract

import au.com.dius.pact.consumer.*
import au.com.dius.pact.consumer.model.MockProviderConfig
import au.com.dius.pact.core.model.RequestResponsePact
import io.micronaut.context.annotation.Value
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.apache.http.client.fluent.Request.Get
import org.junit.jupiter.api.Test


@MicronautTest
class ConsumerTest {

    @Value("\${consumer.client.port}")
    lateinit var expectedPort: String

    @Test
    fun `consumer test using pact dsl`() {

        val pact = ConsumerPactBuilder
                .consumer("the-consumer-pact-dsl")
                .hasPactWith("the-provider")
                .given("specific-state")
                .given("specific-state2")
                .uponReceiving("a simple request")
                .path("/get")
                .query("id=10")
                //inconsistent headers function
                //.headers("Accept","application/json")
                .method("GET")
                .willRespondWith()
                .status(200)
                .body(ResponseDTO("10", "value", 1).toJson())
                //inconsistent headers function
                //.headers("Content-Type","application/json")
                .toPact()

        runTest(pact)
    }

    private fun runTest(pact: RequestResponsePact) {
        val config = mockProviderConfig()
        val result: PactVerificationResult = runConsumerTest(pact, config, object : PactTestRun<Any?> {

            override fun run(mockServer: MockServer, context: PactTestExecutionContext?) {
                // imagining that i have a very complex integration test that uses that stub
                get(mockServer)
            }
        })
        if (result is PactVerificationResult.Error) {
            throw RuntimeException(result.error)
        }
    }

    private fun mockProviderConfig() = MockProviderConfig(hostname = "localhost", port = expectedPort.toInt())

    fun get(mockServer: MockServer): String? =
            Get(mockServer.getUrl() + "/get?id=10").execute().returnContent().asString()
}