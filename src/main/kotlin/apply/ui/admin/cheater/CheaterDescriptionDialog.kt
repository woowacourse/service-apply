package apply.ui.admin.cheater

import apply.application.CheaterResponse
import com.vaadin.flow.component.Text
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout

class CheaterDescriptionDialog(
    cheater: CheaterResponse
) : Dialog() {
    init {
        add(createDescriptionContent(cheater.description))
        open()
    }

    private fun createDescriptionContent(content: String): HorizontalLayout {
        return HorizontalLayout(Text(content)).apply {
            element.style.set("margin", "10px 70px")
            element.style.set("world-break", "break-all")
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        }
    }
}
