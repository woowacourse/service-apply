package apply.ui.admin.cheater

import apply.application.ApplicantService
import apply.application.CheaterService
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import support.views.createContrastButton
import support.views.createPrimaryButton

class CheaterFormDialog(
    private val applicantService: ApplicantService,
    private val cheaterService: CheaterService,
    reloadComponents: () -> Unit
) : Dialog() {
    private val title: H2 = H2("부정 행위자 등록")
    private val cheaterRegistrationForm: CheaterForm = CheaterForm() {
        applicantService.findAllByKeyword(it)
    }

    init {
        add(createHeader(), cheaterRegistrationForm, createButtons(reloadComponents))
        width = "800px"
        height = "60%"
        open()
    }

    private fun createHeader(): VerticalLayout {
        return VerticalLayout(title).apply {
            alignItems = FlexComponent.Alignment.CENTER
            isPadding = false
            element.style.set("margin-bottom", "50px")
        }
    }

    private fun createButtons(reloadComponent: () -> Unit): Component {
        return HorizontalLayout(getCreateAddButton(reloadComponent), createCancelButton()).apply {
            setSizeFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
            element.style.set("margin-top", "20px")
        }
    }

    private fun getCreateAddButton(reloadComponent: () -> Unit): Button {
        return createPrimaryButton("저장") {
            try {
                val bindOrNull = cheaterRegistrationForm.bindOrNull()
                bindOrNull?.let {
                    cheaterService.save(it)
                    reloadComponent()
                    close()
                }
            } catch (e: IllegalArgumentException) {
                createNotification("이미 등록된 부정 행위자입니다.").open()
            } catch (e: NullPointerException) {
                createNotification("대상을 선택해야 합니다.").open()
            }
        }
    }

    private fun createNotification(content: String, durationValue: Int = 1000): Notification {
        return Notification(content).apply {
            duration = durationValue
            position = Notification.Position.MIDDLE
        }
    }

    private fun createCancelButton(): Button {
        return createContrastButton("취소") {
            close()
        }
    }
}