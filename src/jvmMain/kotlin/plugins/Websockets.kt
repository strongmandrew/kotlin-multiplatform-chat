package plugins

import io.ktor.serialization.kotlinx.*
import io.ktor.server.application.*
import io.ktor.server.websocket.*
import kotlinx.serialization.json.Json
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

fun Application.installWs() {
    install(WebSockets) {
        pingPeriod = 10.seconds.toJavaDuration()
        maxFrameSize = Long.MAX_VALUE
        timeout = 20.seconds.toJavaDuration()
        masking = false

        contentConverter = KotlinxWebsocketSerializationConverter(Json)
    }
}