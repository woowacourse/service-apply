package apply.ui.admin.invitation

import apply.application.InvitationService
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.datetimepicker.DateTimePicker
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.textfield.TextField
import support.views.createContrastButton
import support.views.createPrimaryButton
import java.time.LocalDateTime

class InviteAcceptanceDialog(
    private val invitationService: InvitationService,
    private val reloadComponents: () -> Unit = { UI.getCurrent().page.reload() },
) : Dialog() {
    private val keyword: TextField = TextField("키워드").apply {
        placeholder = "저장소 이름에 포함된 단어"
    }
    private val deadlineDateTime: DateTimePicker = DateTimePicker("마감 일시").apply { value = LocalDateTime.now() }

    init {
        add(createHeader(), createForm(), createButtons())
    }

    private fun createHeader(): Component {
        return H2("조건부 수락").apply {
            style["text-align"] = "center"
        }
    }

    private fun createForm(): Component {
        return FormLayout(keyword, deadlineDateTime)
    }

    private fun createButtons(): Component {
        return HorizontalLayout(createCancelButton(), createAcceptButton()).apply {
            style["flex-wrap"] = "wrap"
            style["margin-top"] = "20px"
            justifyContentMode = FlexComponent.JustifyContentMode.END
        }
    }

    private fun createCancelButton(): Component {
        return createContrastButton("취소") {
            close()
        }
    }

    private fun createAcceptButton(): Component {
        return createPrimaryButton("수락") {
            invitationService.acceptAll(keyword.value, deadlineDateTime.value)
            reloadComponents()
            close()
        }
    }
}
