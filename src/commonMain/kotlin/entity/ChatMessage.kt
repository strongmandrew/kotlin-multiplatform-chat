package entity

data class ChatMessage(
    private val user: ChatUser,
    private val content: String,
): ChatEvent()