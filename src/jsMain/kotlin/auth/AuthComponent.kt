package auth

import component.AuthButton
import csstype.Display
import csstype.pct
import emotion.react.css
import react.FC
import react.Props
import react.dom.html.InputType
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.input
import react.useRef

external interface AuthProps : Props {
    var authenticated: Boolean
    var onEnter: (name: String) -> Unit
    var onExit: () -> Unit
}

val Auth = FC<AuthProps> { props ->

    val name = useRef("")

    div {
        css {
            display = Display.grid
            gridAutoColumns = 33.pct
        }

        input {
            this.disabled = props.authenticated
            this.type = InputType.text
            this.placeholder = "Введите своё имя: "
            this.onChange = {
                name.current = it.target.value
            }
        }

        AuthButton {
            disabled = props.authenticated
            onClick = {
                props.onEnter(name.current.orEmpty())
            }
            text = "Войти"
        }

        AuthButton {
            disabled = !props.authenticated
            onClick = {
                props.onExit()
            }
            text = "Выйти"
        }
    }
}