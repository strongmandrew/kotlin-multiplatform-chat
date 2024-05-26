package auth

import csstype.Auto
import csstype.pct
import csstype.px
import emotion.react.css
import react.FC
import react.Props
import react.dom.html.InputType
import react.dom.html.ReactHTML.button
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
            width = 100.pct
            height = 30.pct
            padding = 10.px
        }

        input {
            css {
                width = 70.pct
            }

            this.disabled = props.authenticated
            this.type = InputType.text
            this.placeholder = "Введите своё имя: "
            this.onChange = {
                name.current = it.target.value
            }
        }

        div {
            css {
                float = csstype.Float.right
                width = 15.pct
                height = Auto.auto
                margin = 20.px
            }

            button {
                this.disabled = props.authenticated
                this.onClick = {
                    props.onEnter(name.current.orEmpty())
                }
                +"Войти"
            }

            button {
                this.disabled = !props.authenticated
                this.onClick = {
                    props.onExit()
                }
                +"Выйти"
            }
        }
    }
}