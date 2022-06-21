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
import support.views.createPrimaryButton

class TermFormDialog(
    private val termService: TermService,
    displayName: String,
) : Dialog() {
    private val title: H2 = H2()
    private val termForm: TermForm = TermForm()
    private val submitButton: Button = createSubmitButton()

    init {
        setDisplayName(displayName)
        add(createHeader(), termForm, createButtons())
        width = "800px"
        height = "40%"
        open()
    }

    constructor(termService: TermService, displayName: String, term: TermResponse) : this(termService, displayName) {
        termForm.fill(TermData(term.name, term.id))
    }

    private fun createHeader(): VerticalLayout {
        return VerticalLayout(title).apply {
            alignItems = FlexComponent.Alignment.CENTER
            isPadding = false
            element.style.set("margin-bottom", "10px")
        }
    }

    private fun setDisplayName(displayName: String) {
        title.text = "기수 $displayName"
        submitButton.text = displayName
    }

    private fun createButtons(): Component {
        return HorizontalLayout(submitButton, createCancelButton()).apply {
            setSizeFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
            element.style.set("margin-top", "20px")
        }
    }

    private fun createSubmitButton(): Button {
        return createPrimaryButton {
            termForm.bindOrNull()?.let {
                // TODO : save
                close()
            }
        }
    }

    private fun createCancelButton(): Button {
        return createContrastButton("취소") {
            close()
        }
    }
}
