package chat

import csstype.*
import emotion.react.css
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import utils.messageBackgroundColor
import utils.messageBorderColor
import kotlin.js.Date

external interface MessageProps : Props {
    var message: String
    var from: String
    var timestamp: Long
}

val Message = FC<MessageProps> { props ->

    div {
        css {
            display = Display.flex
            flexDirection = FlexDirection.row
            padding = 10.px
            margin = 10.px
            border = Border(5.px, LineStyle.solid, messageBorderColor)
            borderRadius = 20.px
            backgroundColor = messageBackgroundColor
        }

        div {
            css {
                flex = Flex(number(0.0), number(1.0), 20.pct)
            }

            +"От: ${props.from}"
        }

        div {
            css {
                flex = Flex(number(0.0), number(1.0), 60.pct)
            }

            +props.message
        }

        div {
            css {
                flex = Flex(number(0.0), number(1.0), 20.pct)
            }

            +Date(props.timestamp).toDateString()
        }
    }
}