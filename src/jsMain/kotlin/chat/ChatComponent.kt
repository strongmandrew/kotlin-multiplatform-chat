package chat

import csstype.Color
import csstype.pct
import emotion.react.css
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h2

val Chat = FC<Props> {

    div {
        css {
            height = 150.pct
            width = 250.pct
        }

        h2 {
            css {
                textEmphasisColor = Color("FF0000")
            }
            title = "Чатик"
        }
    }

}