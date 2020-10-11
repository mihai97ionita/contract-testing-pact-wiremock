package ro.contract

import au.com.dius.pact.provider.junit.PactRunner
import au.com.dius.pact.provider.junit5.*
import au.com.dius.pact.provider.junitsupport.Provider
import au.com.dius.pact.provider.junitsupport.State
import au.com.dius.pact.provider.junitsupport.loader.PactBroker
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestTemplate
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.runner.RunWith


@RunWith(PactRunner::class)
@Provider("the-provider")
@PactBroker(
        host = "\${pact.host}",
        scheme = "\${pact.scheme}",
        port = "\${pact.port}"
)
class ProviderContractTestingTest {

    //mocking a real provider
    companion object{
        @JvmStatic
        lateinit var wireMockServer: WireMockServer

        @BeforeAll
        @JvmStatic
        fun `start simulating a provider`(){
            wireMockServer = WireMockServer(8085)
            wireMockServer.start()
        }

        @AfterAll
        @JvmStatic
        fun `stop simulating a provider`(){
            wireMockServer.stop()
        }
    }

    //define a target for executing the provider test on
    @BeforeEach
    fun before(context: PactVerificationContext) {
        context.target = HttpTestTarget("localhost", 8085, "/")
    }

    //running the actual pact test
    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider::class)
    internal fun pactVerificationTestTemplate(context: PactVerificationContext) {
        context.verifyInteraction()
    }

    //defining state 'specific-state'
    @State("specific-state")
    fun `entity with id 10 exists`() {
        wireMockServer.stubFor(WireMock.get(WireMock.urlEqualTo("/get?id=10"))
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(ResponseDTO("10", "value", 1, emptyList()).toJson())
                ))
    }

    @State("specific-state2")
    fun state2(){
    }

}

