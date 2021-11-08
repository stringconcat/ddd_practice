rootProject.name = "ddd_practice"

include(":application")
include(":common:types")
include(":common:telnet")

include(":shop:domain")
include(":shop:usecase")
include(":shop:persistence")
include(":shop:telnet")
include(":shop:web")
include(":shop:payment")
include(":shop:rest")

include(":kitchen:domain")
include(":kitchen:usecase")
include(":kitchen:persistence")
include(":kitchen:web")

include(":delivery") // should be empty