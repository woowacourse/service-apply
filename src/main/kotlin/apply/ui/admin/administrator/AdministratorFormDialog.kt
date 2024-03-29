package apply.ui.admin.administrator

import apply.application.AdministratorData
import apply.application.AdministratorResponse
import apply.application.AdministratorService
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

class AdministratorFormDialog(
    private val administratorService: AdministratorService,
    displayName: String,
    reloadComponents: () -> Unit
) : Dialog() {
    private val title: H2 = H2("관리자 $displayName")
    private val administratorForm: AdministratorForm = AdministratorForm()

    init {
        add(createHeader(), administratorForm, createButtons(displayName, reloadComponents))
        width = "800px"
        open()
    }

    constructor(
        administratorService: AdministratorService,
        displayName: String,
        administrator: AdministratorResponse,
        reloadComponents: () -> Unit
    ) : this(administratorService, displayName, reloadComponents) {
        administratorForm.fill(AdministratorData(administrator.name, administrator.username, id = administrator.id))
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
            setWidthFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
            element.style.set("margin-top", "20px")
        }
    }

    private fun getCreateSubmitButton(displayName: String, reloadComponent: () -> Unit): Component {
        return createPrimaryButton(displayName) {
            administratorForm.bindOrNull()?.let {
                try {
                    administratorService.save(it)
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
