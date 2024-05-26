package ws

import entity.ChatEvent
import io.ktor.client.*
import io.ktor.client.engine.js.*
import io.ktor.client.plugins.websocket.*
import io.ktor.serialization.kotlinx.*
import io.ktor.websocket.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
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
    private val onEvent: (event: ChatEvent) -> Unit
) {
    private var wsJob: Job = Job()

    fun connect(name: String) {
        wsJob = GlobalScope.launch {

            client.webSocket(
                urlString = "ws://localhost:8080/chat",
                request = {
                    url.encodedParameters.append("name", name)
                }
            ) {
                incoming.consumeAsFlow().filterIsInstance<Frame.Text>().map { text ->
                    Json.decodeFromString(
                        deserializer = ChatEvent.serializer(),
                        string = text.readText()
                    )
                }.collect {
                    onEvent(it)
                }
            }
        }
    }

    fun close() {
        wsJob.cancel()
    }
}