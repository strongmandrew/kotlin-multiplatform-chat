package com.strongmandrew.application.ws

import entity.ChatUser
import java.util.UUID

class UserManager {

    private val users = mutableMapOf<String, ChatUser>()

    fun register(user: ChatUser): String =
        UUID.randomUUID().toString().also { uuid -> users += uuid to user }

    fun getByUuid(uuid: String): ChatUser? = users[uuid]
}