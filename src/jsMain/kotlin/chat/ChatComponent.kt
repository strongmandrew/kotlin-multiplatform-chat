package chat

import csstype.*
import emotion.react.css
import entity.SentMessage
import react.FC
import react.Props
import react.dom.html.InputType
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h3
import react.dom.html.ReactHTML.input
import react.useRef

external interface ChatProps : Props {
    var currentUserUuid: String
    var disabled: Boolean
    var onSendMessage: (input: String) -> Unit
    var messages: List<SentMessage>
}

val Chat = FC<ChatProps> { props ->

    val message = useRef("")

    div {
        css {
            flex = Flex(number(0.0), number(1.0), 70.pct)

            display = Display.flex
            flexDirection = FlexDirection.column

            padding = 30.px
        }

        div {
            css {
                flex = Flex(number(0.0), number(1.0), 80.pct)
                overflowY = Overflow.scroll

                display = Display.flex
                flexDirection = FlexDirection.columnReverse
            }

            if (!props.disabled) {

                props.messages.reversed().forEach { message ->

                    Message {
                        from = message.user.name
                        this.message = message.content
                        this.timestamp = message.timestamp
                    }
                }

            } else {
                h3 {
                    css {
                        width = 50.pct
                        margin = Auto.auto

                        flex = Flex(number(0.0), number(1.0), 100.pct)
                        justifyContent = JustifyContent.center
                        textAlign = TextAlign.center
                    }

                    +"Для начала общения необходимо пройти аутентификацию"
                }
            }
        }

        div {
            css {
                flex = Flex(number(0.0), number(1.0), 20.pct)

                display = Display.flex
                flexDirection = FlexDirection.row
            }

            input {
                css {
                    margin = 25.px
                    flex = Flex(number(0.0), number(1.0), 97.pct)
                }
                type = InputType.text
                placeholder = "Напишите сообщение..."
                onChange = { event ->
                    message.current = event.target.value
                }
                disabled = props.disabled
            }

            button {
                css {
                    borderRadius = 25.pct
                    margin = 25.px
                    flex = Flex(number(0.0), number(1.0), 3.pct)
                }

                onClick = {
                    props.onSendMessage(message.current.orEmpty())
                }

                disabled = props.disabled

                +"Отправить"
            }
        }
    }

}