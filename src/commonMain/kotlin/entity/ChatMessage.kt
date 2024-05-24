package entity

import kotlinx.serialization.Serializable

@Serializable
data class ChatMessage(
    private val user: ChatUser,
    private val content: String,
): ChatEvent()