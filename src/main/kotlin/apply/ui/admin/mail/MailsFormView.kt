package apply.ui.admin.mail

import apply.application.EvaluationService
import apply.application.MailHistoryService
import apply.application.MailTargetService
import apply.application.RecruitmentService
import apply.application.UserService
import apply.application.mail.MailService
import apply.ui.admin.BaseLayout
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.BeforeEvent
import com.vaadin.flow.router.HasUrlParameter
import com.vaadin.flow.router.Route
import com.vaadin.flow.router.WildcardParameter
import org.springframework.boot.autoconfigure.mail.MailProperties
import support.views.EDIT_VALUE
import support.views.FORM_URL_PATTERN
import support.views.Title
import support.views.createContrastButton
import support.views.createPrimaryButton

@Route(value = "admin/mails", layout = BaseLayout::class)
class MailsFormView(
    userService: UserService,
    recruitmentService: RecruitmentService,
    evaluationService: EvaluationService,
    mailTargetService: MailTargetService,
    private val mailHistoryService: MailHistoryService,
    private val mailService: MailService,
    mailProperties: MailProperties
) : VerticalLayout(), HasUrlParameter<String> {
    private val mailForm: MailForm = MailForm(
        userService,
        recruitmentService,
        evaluationService,
        mailTargetService,
        mailProperties
    )
    private val submitButton: Component = createSubmitButton()

    init {
        add(Title("메일"), mailForm, createButtons())
    }

    override fun setParameter(event: BeforeEvent, @WildcardParameter parameter: String) {
        val result = FORM_URL_PATTERN.find(parameter) ?: return UI.getCurrent().page.history.back()
        val (id, value) = result.destructured
        if (value == EDIT_VALUE) {
            mailForm.fill(mailHistoryService.getById(id.toLong()))
            submitButton.isVisible = false
        }
    }

    private fun createButtons(): Component {
        return HorizontalLayout(submitButton, createCancelButton()).apply {
            setSizeFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        }
    }

    private fun createSubmitButton(): Button {
        return createPrimaryButton("보내기") {
            mailForm.bindOrNull()?.let {
                mailHistoryService.save(it)
                mailService.sendMailsByBcc(it, it.attachFiles)
                UI.getCurrent().navigate(MailsView::class.java)
            }
        }
    }

    private fun createCancelButton(): Button {
        return createContrastButton("취소") {
            UI.getCurrent().navigate(MailsView::class.java)
        }
    }
}
