package apply.ui.admin.mail

import apply.application.EvaluationService
import apply.application.MailHistoryService
import apply.application.MailTargetService
import apply.application.MemberService
import apply.application.RecruitmentService
import apply.application.mail.MailData
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
import support.views.createNotification
import support.views.createPrimaryButton

private const val NO_RECIPIENT_MESSAGE: String = "받는사람을 한 명 이상 지정해야 합니다."

@Route(value = "admin/mails", layout = BaseLayout::class)
class MailsFormView(
    memberService: MemberService,
    recruitmentService: RecruitmentService,
    evaluationService: EvaluationService,
    mailTargetService: MailTargetService,
    private val mailHistoryService: MailHistoryService,
    private val mailService: MailService,
    mailProperties: MailProperties
) : VerticalLayout(), HasUrlParameter<String> {
    private val mailForm: MailForm = MailForm(
        memberService,
        recruitmentService,
        evaluationService,
        mailTargetService,
        mailProperties
    )
    private val submitButton: Component = createSubmitButton()

    init {
        add(Title("메일 쓰기"), mailForm, createButtons())
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
        return HorizontalLayout(submitButton, createCancelButton(), createPreviewButton()).apply {
            setSizeFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        }
    }

    private fun createSubmitButton(): Button {
        return createPrimaryButton("보내기") {
            handleMailData { mail ->
                mailService.sendMailsByBcc(mail, mail.attachments)
                UI.getCurrent().navigate(MailsView::class.java)
            }
        }
    }

    private fun createCancelButton(): Button {
        return createContrastButton("취소") {
            UI.getCurrent().navigate(MailsView::class.java)
        }
    }

    private fun createPreviewButton(): Button {
        return createContrastButton("미리 보기") {
            handleMailData { mail -> MailPreviewDialog(mailService.generateMailBody(mail)) }
        }
    }

    private fun handleMailData(action: (MailData) -> Unit) {
        val result = mailForm.bindOrNull()
        if (result == null) {
            createNotification("받는사람을 한 명 이상 지정해야 합니다.")
        } else {
            action(result)
        }
    }
}
