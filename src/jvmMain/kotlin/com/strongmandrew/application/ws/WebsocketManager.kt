package com.strongmandrew.application.ws

import entity.ChatUser
import entity.UserAuthCompleted
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.isActive
import java.util.*

class WebsocketManager {

    private val users = mutableMapOf<String, ChatUser>()
    val sessions: MutableList<WebSocketServerSession> = mutableListOf()

    suspend fun connect(user: ChatUser, session: WebSocketServerSession) {
        val userAuthCompleted = register(user).let(::UserAuthCompleted)

        sessions += session

        session.sendSerialized(userAuthCompleted)
    }

    suspend inline fun <reified T> send(event: T) {
        val lostConnections = mutableListOf<WebSocketSession>()

        sessions.forEach { session ->
            if (session.isActive) {
                session.sendSerialized(event)
            } else {
                lostConnections += session
            }
        }

        lostConnections.forEach { session ->
            session.close(CloseReason(CloseReason.Codes.NORMAL, "Закрытие"))
        }
    }

    private fun register(user: ChatUser): String =
        UUID.randomUUID().toString().also { uuid -> users += uuid to user }

    fun getByUuid(uuid: String): ChatUser? = users[uuid]
}