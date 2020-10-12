package ro.contract.util

import com.atlassian.ta.wiremockpactgenerator.WireMockPactGenerator
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File

fun WireMockPactGenerator.injectStates(states: List<String>): WireMockPactGenerator{
    val file = File(pactFilePath(this))
    val fileContent = file.readText()
    val topLevelObjects = ObjectMapper().readValue<MutableMap<Any, Any>>(fileContent)
    addStates(topLevelObjects, states)
    addMetadata(topLevelObjects)
    file.writeText(topLevelObjects.toJson())
    return this
}

private data class Version(val version: String)
private data class Metadata(val pactSpecification: Version, val `pact-jvm`: Version)
fun addMetadata(topLevelObjects: MutableMap<Any, Any>) {
    //TODO investigate pact-jvm version.. should not be hardcoded?
    topLevelObjects["metadata"] = Metadata(
             Version("3.0.0"), Version("4.1.0")
    )
}

private data class State(val name: String)
private fun addStates(topLevelObjects: MutableMap<Any, Any>, states: List<String>) {
    val providerStates: List<State> = states.map { State(it) }
    val interactionsObj = topLevelObjects["interactions"] as MutableList<MutableMap<Any, Any>>
    interactionsObj.forEach {
        it["providerStates"] = providerStates
    }
    topLevelObjects["interactions"] = interactionsObj
}


private fun pactFilePath(wire: WireMockPactGenerator) = File("").absolutePath+ "/" + wire.pactLocation
private fun Any.toJson(): String = ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(this)
