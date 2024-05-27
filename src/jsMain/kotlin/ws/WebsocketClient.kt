package ws

import entity.ClientEvent
import entity.ServerEvent
import io.ktor.client.*
import io.ktor.client.engine.js.*
import io.ktor.client.plugins.websocket.*
import io.ktor.serialization.kotlinx.*
import io.ktor.websocket.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class WebsocketClient(
    private val client: HttpClient = HttpClient(Js) {
        WebSockets {
            this.contentConverter = KotlinxWebsocketSerializationConverter(Json)
            this.pingInterval = 10
            this.maxFrameSize = Long.MAX_VALUE
        }
    },
    private val onEvent: (event: ServerEvent) -> Unit,
) {

    private var currentSession: DefaultClientWebSocketSession? = null
        set(value) {
            console.log("Установил значение")
            field = value
        }

    fun connect(name: String) {
        GlobalScope.launch {

            val session = client.webSocketSession(
                urlString = "ws://localhost:8080/chat",
            ) {
                url.encodedParameters.append("name", name)
            }

            currentSession = session

            console.log("Присвоил сессию. Иду дальше")

            session.incoming.consumeAsFlow().filterIsInstance<Frame.Text>().map { text ->
                Json.decodeFromString(
                    deserializer = ServerEvent.serializer(),
                    string = text.readText()
                )
            }.collect {
                onEvent(it)
            }
        }
    }

    fun send(event: ClientEvent) {
        GlobalScope.launch {
            console.log("ОТправка события. Сессия естЬ? ${currentSession}")

            val frameText = Frame.Text(
                Json.encodeToString(
                    serializer = ClientEvent.serializer(),
                    value = event
                )
            )

            currentSession?.send(frameText)
        }
    }

    fun close() {
        GlobalScope.launch {
            currentSession?.close(CloseReason(code = CloseReason.Codes.NORMAL, ""))
            currentSession = null
        }
    }
}