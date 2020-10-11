package ro.contract

import com.atlassian.ta.wiremockpactgenerator.WireMockPactGenerator
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import io.micronaut.context.annotation.Value
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import ro.contract.util.injectStates
import javax.inject.Inject


@MicronautTest
class ConsumerWiremockTest{

    @Inject
    lateinit var consumerClient: ConsumerClient

    @Value("\${consumer.client.host}")
    private lateinit var host: String

    @Value("\${consumer.client.port}")
    private lateinit var port: String

    //@Deprecated("using WireMockPactGenerator does not provide state for pact tests")
    @Test
    fun `generate pact file using wiremock`() {
        // wiremock setup
        val wireMockServer = WireMockServer(port.toInt())
        wireMockServer.start()

        //wiremock pact generator setup
        val pact = WireMockPactGenerator.builder("the-consumer-wiremock", "the-provider").build()
        wireMockServer.addMockServiceRequestListener(pact)
        //this pact is attached to the server, so you would need a new wiremock for each test/ use junit4 @Rule (same thing)


        // usual wiremock stubbing
        wireMockServer.stubFor(get(urlEqualTo("/get?id=10"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(ResponseDTO("10", "value", 1).toJson())
                ))

        // when
        // imagining that i have a very complex integration test that uses that stub
        val result = consumerClient.get("10").blockingGet()

        //inject after file was generated, after test
        //hackish way of adding state to the generated pact file
        pact.injectStates(listOf("specific-state","specific-state2"))


        assertThat(result.field1).isEqualTo("value")
        assertThat(result.number1).isEqualTo(1)
        wireMockServer.stop()
    }
    
}
