package entity

sealed class ChatEvent

data class UserAuthCompleted(
    val userUuid: String,
    val latestMessages: List<ChatMessage>
) : ChatEvent()

data class SendChatMessage(
    private val userUuid: String,
    private val content: String,
) : ChatEvent()

