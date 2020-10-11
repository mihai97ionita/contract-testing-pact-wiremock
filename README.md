# Contract testing POC using Pact framework

- Contract testing is done from the consumer and provider perspective with a pact broker between them.
- Pact broker is turned on by `docker-compose up` and running on `localhost:9292`
- Consumer is creating the contract (by pact dsl or wiremock + conversion to pact) -> run unit tests against stubs -> publish to pact broker is done by a gradle plugin
- Provider is pulling pact files from broker by starting tests -> set the necessary states -> run tests against it's system (just a mock in this case) -> publish verification result to pact broker

# Thoughts and conclusions
- Viable E2E testing replacement
- Incredible practical potential
- Unpolished syntax
- Difficult to configure
- Consumer creating expectations from Provider, expressing them in a pact file, Provider checking expectations on Consumer behalf     

### Consumer side
  - Should be done by developers (run simple 'integration' unit tests against pact stubs, thus resulting in functionality checking the contract against your microservice)
  - Use pact dsl consumer instead of wiremock + conversion to pact, it will always give you more functionality
  - It's not replacement for wiremock, as all pact test must use @State by Provider, thus limiting possible scenarios
  - Write dedicated tests for contract testing covering the essential functionality using provided @State of Provider

### Provider side
  - Should be done by testers (pact will test Consumer's expectations described by the pact file against a real environment, provide @State that can be accomplished in that environment for Consumer pacts)
  - Pact is doing most of the job, you just need to provide him a http client to check the pacts (run the pact tests) and provide states (@State) for the consumers to use




# Built using
- Kotlin
- Micronaut
- Pact
- Junit5
- Docker
