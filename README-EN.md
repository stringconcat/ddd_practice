# Example application for the course [Developing Enterprise Applications Without Pain and Regret](https://howto.stringconcat.ru)
## Architecture
The application is using microservice architecture, each service encapsulates a bounded contexts.
In the [part_1](https://github.com/stringconcat/ddd_practice/tree/part_1) branch, you can see how the application starts as modular monolith at the early stage.

Each microservice is implemented according to the principles of Clean Architecture,
and the business logic is implemented using Domain-Driven Design patterns (Aggregates, Entities, Value Objects, Domain Services).
The project structure is maintained by [ArchUnit](https://www.archunit.org/).

Currently, a RDMS is used only for a single aggregate,
everything else is an InMemory implementation of repositories.

We also tried to implement the principle of Screaming Architecture.
The project structure reflects the purpose of the project and its functionality,
the same applies to packages and even individual [files](shop/usecase/src/main/kotlin/com/stringconcat/ddd/shop/usecase/cart).

Decisions about important architectural choices are contained in [Architecture Decision Records](/tools/adr).

## Infrastructure
### Continuous Integration
The main idea is that it should be as convenient as mush as possible for developers
to work at their local computer. Therefore, all necessary actions with the project
can be performed by pressing a single "button" located in [tools/scripts](tools/scripts).
Thus, we fully implement the CI concept.
[GitHubActions](https://github.com/stringconcat/ddd_practice/actions) is used as a safeguard.
If the build fails, the reports artifact contains all the necessary information for diagnostics.
For push, you can install a [pre-push hook](tools/scripts/installHook.sh),
which will run the entire build before pushing to the repository.

### Build
In addition to simple compilation and image preparation,
the project uses additional tools, the results of which also affect the success or failure of the build.

* Static analyzer [detekt](https://detekt.dev/)
* Linter - [ktlint](https://ktlint.github.io/), (as a plugin to detekt)
* Test coverage control [JaCoCo](https://github.com/jacoco/jacoco)
* Vulnerable dependencies search [OWASP DependencyCheck](https://jeremylong.github.io/DependencyCheck/)
* Search for new versions for dependencies []()
* Control of the absence of compiler warnings

Dependencies are listed in [ProjectDependencies.kt](buildSrc/src/main/kotlin/ProjectDependencies.kt)

### Launch
With "buttons" from [tools/scripts](tools/scripts), you can deploy and
stop the cluster on the developer's machine
without the need to occupy dev\sit\uat environments.

## Tests
The test pyramid implemented in the project:
* Unit tests
* Integration tests
  * Testing repositories
  * Testing clients to external services
* Contract tests [Pact.io](https://pact.io)
* Component tests
* End-to-end tests [kbdd](https://github.com/ru-fix/kbdd/) + [Allure](https://docs.qameta.io/allure/)
* Project structure tests [ArchUnit](https://www.archunit.org/)
* Mutation tests [PiTest](https://pitest.org/)
* Performance tests [Gatling](https://gatling.io/)

## Interaction with the external world
The example implements:
* Asynchronous interaction between microservices via [RabbitMQ](https://rabbitmq.com)
* HATEOAS level REST - [Spring HATEOAS](https://spring.io/projects/spring-hateoas)
* Use of resilience patterns ([for example, the library](https://resilience4j.readme.io/))
* Centralized error handling
* Input validation by the domain model
* Interaction via Telnet ([ktelnet library, copied to the repository with several bug fixes](https://github.com/teverett/ktelnet))

## Event Storming
[Miro board](https://miro.com/app/board/o9J_lR8tnlI=/)

## Monitoring
In Progress

## Configuration
In Progress

## Security
In Progress

Star us and join our [Telegram channel](https://t.me/stringconcat)