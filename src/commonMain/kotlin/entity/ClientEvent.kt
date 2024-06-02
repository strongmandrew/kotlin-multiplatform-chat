package entity

import kotlinx.serialization.Serializable

@Serializable
sealed class ClientEvent

@Serializable
data class SendMessage(
    val userUuid: String,
    val content: String,
) : ClientEvent()

@Serializable
sealed class ServerEvent

@Serializable
data class AuthCompleted(
    val userUuid: String,
) : ServerEvent()

@Serializable
data class SentMessage(
    val user: ChatUser,
    val content: String,
    val timestamp: Long
): ServerEvent()
