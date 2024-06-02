package entity

import kotlinx.serialization.Serializable

@Serializable
data class ChatUser(
    val uuid: String = "",
    val name: String
)