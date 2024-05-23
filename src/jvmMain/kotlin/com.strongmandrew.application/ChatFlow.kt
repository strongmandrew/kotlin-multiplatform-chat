package com.strongmandrew.application

import entity.ChatMessage
import kotlinx.coroutines.flow.MutableSharedFlow

class ChatFlow(replayCache: Int) {

    constructor(replayCache: Int? = null) : this(replayCache ?: DEFAULT_REPLAY_SIZE)

    companion object {
        const val DEFAULT_REPLAY_SIZE = 10
    }

    val messages = MutableSharedFlow<ChatMessage>(
        replay = replayCache
    )
}