package entity

import kotlinx.serialization.Serializable

@Serializable
data class ChatMessage(
    val user: ChatUser,
    val content: String,
): ChatEvent()