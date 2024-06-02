package com.strongmandrew.application.ws

import entity.AuthCompleted
import entity.ChatUser
import entity.ServerEvent
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.serialization.json.Json
import java.util.*

class WebsocketManager {

    private val users = mutableMapOf<String, ChatUser>()

    suspend fun connect(user: ChatUser, session: WebSocketServerSession) {
        val authCompleted = register(user).let(::AuthCompleted)

        session.send(
            Frame.Text(
                Json.encodeToString(
                    serializer = ServerEvent.serializer(),
                    value = authCompleted
                )
            )
        )
    }

    suspend fun <T : ServerEvent> send(event: T, session: WebSocketSession) {
        session.send(
            Frame.Text(
                Json.encodeToString(
                    serializer = ServerEvent.serializer(),
                    value = event
                )
            )
        )
    }

    private fun register(user: ChatUser): String =
        UUID.randomUUID().toString().also { uuid -> users += uuid to user }

    fun getByUuid(uuid: String): ChatUser? = users[uuid]
}