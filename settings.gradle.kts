rootProject.name = "ddd_practice"

include(":application")
include(":common:types")

include(":shop:domain")
include(":shop:usecase")
include(":shop:persistence")
include(":shop:telnet")
include(":shop:web")

include(":kitchen:domain")
include(":kitchen:usecase")
include(":kitchen:persistence")
include(":kitchen:web")

include(":integration:payment")
include(":integration:telnet")