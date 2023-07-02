package real.cool.screen.server.core

import java.util.UUID

data class Device(
    val id: String = UUID.randomUUID().toString(),
    var supportInputEvent: Boolean = true,
    var width: Int = 500,
    var height: Int = 800
)