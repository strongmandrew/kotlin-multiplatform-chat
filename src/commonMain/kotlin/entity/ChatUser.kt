package entity

import kotlinx.serialization.Serializable

@Serializable
data class ChatUser(
    val name: String,
    val surname: String
)