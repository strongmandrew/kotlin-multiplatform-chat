package chat

import csstype.*
import emotion.react.css
import react.FC
import react.Props
import react.dom.html.InputType
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h2
import react.dom.html.ReactHTML.input
import react.useRef

external interface ChatProps : Props {
    var onSendMessage: (input: String) -> Unit
}

val Chat = FC<ChatProps> { props ->

    val message = useRef("")

    div {
        css {
            flex = Flex(number(0.0), number(1.0), 70.pct)

            display = Display.flex
            flexDirection = FlexDirection.column
        }

        div {
            css {
                flex = Flex(number(0.0), number(1.0), 80.pct)
            }
            h2 {
                +"Another"
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

                +"Send"
            }
        }
    }

}