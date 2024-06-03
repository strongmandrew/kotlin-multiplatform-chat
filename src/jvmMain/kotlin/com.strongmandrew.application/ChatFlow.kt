package com.strongmandrew.application

import com.strongmandrew.application.logger.getLogger
import entity.SentMessage
import kotlinx.coroutines.flow.MutableSharedFlow

private const val DEFAULT_REPLAY_CACHE = 10

fun <T : Any> T.createChatFlow(): MutableSharedFlow<SentMessage> =
    System.getenv("replay.size")?.toIntOrNull().let {
        it ?: DEFAULT_REPLAY_CACHE
    }.also {
        getLogger().info("Размер истории последних сообщений: $it")
    }.let { MutableSharedFlow(replay = it) }
