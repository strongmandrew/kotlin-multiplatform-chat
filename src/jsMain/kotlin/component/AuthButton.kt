package component

import csstype.pct
import csstype.px
import emotion.react.css
import react.FC
import react.Props
import react.dom.html.ReactHTML.button
import utils.defaultColor

external interface AuthButtonProps : Props {
    var text: String
    var disabled: Boolean
    var onClick: () -> Unit
}

val AuthButton = FC<AuthButtonProps> { props ->

    button {
        css {
            width = 100.px
            height = 30.px
            backgroundColor = defaultColor
            padding = 10.px
            borderRadius = 10.pct
        }

        onClick = {
            props.onClick()
        }

        disabled = props.disabled

        +props.text
    }
}