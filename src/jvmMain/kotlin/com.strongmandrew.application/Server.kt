package com.strongmandrew.application

import com.strongmandrew.application.ws.WebsocketManager
import entity.ChatUser
import entity.ClientEvent
import entity.SendMessage
import entity.SentMessage
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.util.*
import io.ktor.websocket.*
import kotlinx.coroutines.flow.*
import kotlinx.html.*
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import plugins.installWs

fun HTML.index() {
    head {
        title("Hello from Ktor!")
    }
    body {
        div {
            +"Hello from Ktor"
        }
        div {
            id = "root"
        }
    }
}

fun main() {
    embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
        module()
    }.start(wait = true)
}

private fun Application.module() {

    val chatFlow = ChatFlow.fromEnvironment(environment)
    val websocketManager = WebsocketManager()

    installWs()
    routing {
        get {

        }

        webSocket("/chat") {
            val queryParams = call.request.queryParameters.toMap()

            val name = queryParams["name"]?.firstOrNull() ?: return@webSocket

            val chatUser = ChatUser(name)

            websocketManager.connect(chatUser, this)

            chatFlow.messages.onEach {
                websocketManager.send(it)
            }.launchIn(this)

            incoming.consumeAsFlow().filterIsInstance<Frame.Text>().map { frame ->
                val frameText = frame.readText()
                LoggerFactory.getLogger(this::class.java).info(frameText)
                Json.decodeFromString<ClientEvent>(frameText)
            }.filterIsInstance<SendMessage>().mapNotNull { sendMessage ->
                val actualUser = websocketManager.getByUuid(sendMessage.userUuid) ?: return@mapNotNull null
                SentMessage(actualUser, sendMessage.content)
            }.collect(chatFlow.messages)
        }
    }
}