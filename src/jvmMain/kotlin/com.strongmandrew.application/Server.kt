package com.strongmandrew.application

import com.strongmandrew.application.ws.UserManager
import com.strongmandrew.application.ws.WebsocketManager
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import kotlinx.html.*
import plugins.installWs

fun HTML.index() {
    head {
        title("Hello from Ktor!")
    }
    body {
        div {
            +"Hello from Ktor"
        }
        div {
            id = "root"
        }
    }
}

fun main() {
    embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
        module()
    }.start(wait = true)
}

private fun Application.module() {

    val chatFlow = environment.config.toMap()["replay.size"].toString().toIntOrNull().let(::ChatFlow)
    val userManager = UserManager()

    installWs()
    routing {
        get("/") {
            call.respondHtml(HttpStatusCode.OK) {
                index()
            }
        }

        webSocket {

        }
    }
}