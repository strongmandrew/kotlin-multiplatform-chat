package com.strongmandrew.application.ws

import com.strongmandrew.application.ChatFlow
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow

class WebsocketManager<T : Any>(
    private val chatFlow: ChatFlow
) {

    private val sessions: MutableList<WebSocketServerSession> = mutableListOf()

    suspend fun connect(session: WebSocketServerSession) {
        sessions += session
    }

    fun send(event: T) {
        sessions.forEach { session ->

        }
    }
}