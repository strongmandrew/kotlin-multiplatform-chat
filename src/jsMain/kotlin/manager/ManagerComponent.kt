package manager

import auth.Auth
import chat.Chat
import csstype.Display
import csstype.JustifyContent
import csstype.vh
import emotion.react.css
import entity.ChatMessage
import entity.UserAuthCompleted
import kotlinx.browser.sessionStorage
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.useState
import ws.WebsocketClient

val Manager = FC<Props> {

    val (currentUserUuid, setCurrentUserUuid) = useState(sessionStorage.getItem("userUuid"))

    val wsClient = WebsocketClient { event ->
        when (event) {
            is UserAuthCompleted -> {
                sessionStorage.setItem("userUuid", event.userUuid)
                setCurrentUserUuid(event.userUuid)
            }

            is ChatMessage -> {
                console.log("Сообщение от ${event.user} содержания ${event.content}")
            }

            else -> {}
        }
    }

    div {
        css {
            display = Display.flex
            height = 100.vh
            justifyContent = JustifyContent.spaceBetween
        }

        Auth {
            authenticated = currentUserUuid != null
            onEnter = {
                wsClient.connect(it)
            }
            onExit = {
                wsClient.close()
                sessionStorage.removeItem("userUuid")
                setCurrentUserUuid(null)
            }
        }
        Chat()
    }


}