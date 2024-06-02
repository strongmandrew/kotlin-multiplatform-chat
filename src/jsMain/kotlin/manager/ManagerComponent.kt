package manager

import auth.Auth
import chat.Chat
import csstype.Display
import csstype.JustifyContent
import csstype.vh
import emotion.react.css
import entity.AuthCompleted
import entity.SendMessage
import entity.SentMessage
import kotlinx.browser.sessionStorage
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.useEffectOnce
import react.useState
import ws.WebsocketClient

private const val USER_UUID_KEY = "userUuid"
private const val USERNAME_KEY = "username"

val Manager = FC<Props> {

    val (currentUserUuid, setCurrentUserUuid) = useState(sessionStorage.getItem(USER_UUID_KEY))
    val (currentUsername, setCurrentUsername) = useState(sessionStorage.getItem(USERNAME_KEY))
    val (currentMessages, setMessages) = useState<List<SentMessage>>()

    val wsClient = WebsocketClient { event ->
        when (event) {
            is AuthCompleted -> {
                sessionStorage.setItem(USER_UUID_KEY, event.userUuid)
                setCurrentUserUuid(event.userUuid)
            }

            is SentMessage -> {
                setMessages { previous ->
                    previous.orEmpty() + event
                }
            }
        }
    }

    useEffectOnce {
        currentUsername?.let(wsClient::connect)
    }

    div {
        css {
            display = Display.flex
            height = 100.vh
            justifyContent = JustifyContent.spaceBetween
        }

        Auth {
            this.currentUsername = currentUsername
            onEnter = {
                sessionStorage.setItem(USERNAME_KEY, it)
                setCurrentUsername(it)
                wsClient.connect(it)
            }
            onExit = {
                wsClient.close()
                sessionStorage.removeItem(USER_UUID_KEY)
                setCurrentUserUuid(null)
                sessionStorage.removeItem(USERNAME_KEY)
                setCurrentUsername(null)
            }
        }
        Chat {
            this.currentUserUuid = currentUserUuid.orEmpty()
            messages = currentMessages.orEmpty()
            disabled = currentUserUuid?.isEmpty() ?: true
            onSendMessage = {
                val sendMessageEvent = SendMessage(
                    userUuid = currentUserUuid.orEmpty(),
                    content = it
                )
                wsClient.send(sendMessageEvent)
            }
        }
    }
}