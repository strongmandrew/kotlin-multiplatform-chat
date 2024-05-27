package com.strongmandrew.application

import entity.SentMessage
import io.ktor.server.application.*
import kotlinx.coroutines.flow.MutableSharedFlow

class ChatFlow(replayCache: Int) {

    constructor(replayCache: Int? = null) : this(replayCache ?: DEFAULT_REPLAY_SIZE)

    companion object {
        const val DEFAULT_REPLAY_SIZE = 10
    }

    val messages = MutableSharedFlow<SentMessage>(
        replay = replayCache
    )
}

fun ChatFlow.Companion.fromEnvironment(environment: ApplicationEnvironment): ChatFlow =
    environment.config.toMap()["replay.size"].toString().toIntOrNull().let(::ChatFlow)
