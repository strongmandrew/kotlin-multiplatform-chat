import kotlinx.browser.document
import manager.Manager
import react.create
import react.dom.client.createRoot

fun main() {
    val container = document.body ?: error("Тело документа не найдено!")
    createRoot(container).render(Manager.create())
}