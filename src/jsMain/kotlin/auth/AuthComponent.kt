package auth

import component.AuthButton
import csstype.*
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
            flex = Flex(number(0.0), number(1.0), 30.pct)
            display = Display.flex
            flexDirection = FlexDirection.column
            justifyContent = JustifyContent.center
        }

        input {
            this.disabled = props.authenticated
            this.type = InputType.text
            this.placeholder = "Введите своё имя: "
            this.onChange = {
                name.current = it.target.value
            }
        }

        div {
            css {
                display = Display.flex
                justifyContent = JustifyContent.spaceAround
                flexDirection = FlexDirection.row
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
}