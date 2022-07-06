package apply.ui.admin.term

import apply.application.TermData
import apply.application.TermResponse
import apply.application.TermService
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import support.views.createContrastButton
import support.views.createNotification
import support.views.createPrimaryButton

class TermFormDialog(
    private val termService: TermService,
    displayName: String,
    reloadComponents: () -> Unit
) : Dialog() {
    private val title: H2 = H2()
    private val termForm: TermForm = TermForm()

    init {
        title.text = "기수 $displayName"
        add(createHeader(), termForm, createButtons(displayName, reloadComponents))
        width = "800px"
        height = "40%"
        open()
    }

    constructor(termService: TermService, displayName: String, term: TermResponse, reloadComponents: () -> Unit) : this(
        termService,
        displayName,
        reloadComponents
    ) {
        termForm.fill(TermData(term.name, term.id))
    }

    private fun createHeader(): VerticalLayout {
        return VerticalLayout(title).apply {
            alignItems = FlexComponent.Alignment.CENTER
            isPadding = false
            element.style.set("margin-bottom", "10px")
        }
    }

    private fun createButtons(displayName: String, reloadComponent: () -> Unit): Component {
        return HorizontalLayout(getCreateSubmitButton(displayName, reloadComponent), createCancelButton()).apply {
            setSizeFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
            element.style.set("margin-top", "20px")
        }
    }

    private fun getCreateSubmitButton(displayName: String, reloadComponent: () -> Unit): Component {
        return createPrimaryButton(displayName) {
            termForm.bindOrNull()?.let {
                try {
                    termService.save(it)
                    reloadComponent()
                    close()
                } catch (e: Exception) {
                    createNotification(e.localizedMessage)
                }
            }
        }
    }

    private fun createCancelButton(): Button {
        return createContrastButton("취소") {
            close()
        }
    }
}
