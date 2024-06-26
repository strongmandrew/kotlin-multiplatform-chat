package com.strongmandrew.application

import com.strongmandrew.application.logger.getLogger
import com.strongmandrew.application.plugins.installWs
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
import kotlinx.serialization.json.Json
import java.time.Instant

fun main() {
    embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
        module()
    }.start(wait = true)
}

private fun Application.module() {

    val logger = getLogger()
    val chatFlow = createChatFlow()
    val websocketManager = WebsocketManager()

    installWs()

    routing {
        webSocket("/chat") {
            val queryParams = call.request.queryParameters.toMap()

            val name = queryParams["name"]?.firstOrNull() ?: let {
                logger.warning("Не было предоставлено имя пользователя для подключения к чату")
                return@webSocket
            }

            val chatUser = ChatUser(name = name)

            websocketManager.connect(chatUser, this)

            chatFlow.onEach {
                websocketManager.send(it, this)
            }.launchIn(this)

            incoming.consumeAsFlow().filterIsInstance<Frame.Text>().map { frame ->
                val frameText = frame.readText()
                Json.decodeFromString<ClientEvent>(frameText)
            }.filterIsInstance<SendMessage>().mapNotNull { sendMessage ->
                val actualUser = websocketManager.getByUuid(sendMessage.userUuid) ?: let {
                    logger.warning("Не найден пользователь по [uuid=${sendMessage.userUuid}]")
                    return@mapNotNull null
                }
                SentMessage(
                    actualUser,
                    sendMessage.content,
                    Instant.now().toEpochMilli()
                )
            }.collect(chatFlow)
        }
    }
}