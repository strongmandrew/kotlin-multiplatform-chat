package ws

import entity.ClientEvent
import entity.ServerEvent
import io.ktor.client.*
import io.ktor.client.engine.js.*
import io.ktor.client.plugins.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

private var currentSession: DefaultClientWebSocketSession? = null

class WebsocketClient(
    private val client: HttpClient = HttpClient(Js) {
        WebSockets {
            this.pingInterval = 10
            this.maxFrameSize = Long.MAX_VALUE
        }
    },
    private val onEvent: (event: ServerEvent) -> Unit,
) {

    fun connect(name: String) {
        GlobalScope.launch {

            val session = client.webSocketSession(
                urlString = "ws://localhost:8080/chat",
            ) {
                url.encodedParameters.append("name", name)
            }

            currentSession = session

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
            currentSession?.close(
                CloseReason(code = CloseReason.Codes.NORMAL, "Клиент закрыл сессию")
            )
            currentSession = null
        }
    }
}