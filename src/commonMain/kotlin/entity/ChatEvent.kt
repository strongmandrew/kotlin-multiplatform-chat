package entity

import kotlinx.serialization.Serializable

@Serializable
sealed class ChatEvent

@Serializable
data class UserAuthCompleted(
    val userUuid: String,
) : ChatEvent()

@Serializable
data class SendChatMessage(
    val userUuid: String,
    val content: String,
) : ChatEvent()

