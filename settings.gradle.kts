rootProject.name = "ddd_practice"

include(":application")
include(":common:types")

include(":order:domain")
include(":order:usecase")
include(":order:persistence")

include(":kitchen:domain")
include(":kitchen:usecase")
include(":kitchen:persistence")

include(":integration:payment")
include(":integration:telnet")
include(":integration:dummyCRM")
