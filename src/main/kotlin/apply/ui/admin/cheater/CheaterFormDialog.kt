package apply.ui.admin.cheater

import apply.application.CheaterService
import apply.application.MemberService
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

class CheaterFormDialog(
    private val memberService: MemberService,
    private val cheaterService: CheaterService,
    reloadComponents: () -> Unit
) : Dialog() {
    private val title: H2 = H2("부정행위자 등록")
    private val cheaterRegistrationForm: CheaterForm = CheaterForm { memberService.findAllByKeyword(it) }

    init {
        add(createHeader(), cheaterRegistrationForm, createButtons(reloadComponents))
        width = "800px"
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
            setWidthFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
            element.style.set("margin-top", "20px")
        }
    }

    private fun getCreateAddButton(reloadComponent: () -> Unit): Button {
        return createPrimaryButton("저장") {
            try {
                cheaterRegistrationForm.bindOrNull()?.let {
                    cheaterService.save(it)
                    reloadComponent()
                    close()
                }
            } catch (e: IllegalArgumentException) {
                createNotification("이미 등록된 부정행위자입니다.")
            } catch (e: NullPointerException) {
                createNotification("대상을 선택해야 합니다.")
            }
        }
    }

    private fun createCancelButton(): Button {
        return createContrastButton("취소") {
            close()
        }
    }
}
