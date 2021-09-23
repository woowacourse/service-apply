package apply.ui.admin.mail

import apply.application.ApplicantService
import apply.application.EvaluationService
import apply.application.MailTargetResponse
import apply.application.MailTargetService
import apply.application.RecruitmentService
import apply.application.mail.MailSendData
import apply.ui.admin.BaseLayout
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.Key
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.html.H3
import com.vaadin.flow.component.html.H4
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer
import com.vaadin.flow.router.Route
import org.springframework.beans.factory.annotation.Value
import support.views.BindingFormLayout
import support.views.Title
import support.views.addSortableColumn
import support.views.createNormalButton
import support.views.createPrimaryButton
import support.views.createSearchBar
import support.views.createUploadButton

@Route(value = "admin/emails", layout = BaseLayout::class)
class MailFormView(
    private val applicantService: ApplicantService,
    private val recruitmentService: RecruitmentService,
    private val evaluationService: EvaluationService,
    private val mailTargetService: MailTargetService,
    @Value("\${spring.mail.username}")
    private val senderEmail: String
) : BindingFormLayout<MailSendData>(MailSendData::class) {
    private val subject: TextField = TextField()
    private val recipients: MutableList<String> = mutableListOf()
    private val content: TextArea = createMailBody()
    private val mailTargetGrid: Grid<MailTargetResponse> = createMailTargetsGrid()

    private fun createMailTargetsGrid(): Grid<MailTargetResponse> {
        return Grid<MailTargetResponse>(10).apply {
            addSortableColumn("이메일", MailTargetResponse::email)
        }
    }

    init {
        add(Title("메일 발송"), createMailForm())
        setWidthFull()
        setResponsiveSteps(ResponsiveStep("0", 1))
        drawRequired()
    }

    private fun createTitle(): Component {
        return HorizontalLayout(H1("모집 관리")).apply {
            setSizeFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        }
    }

    private fun createMailForm(): Component {
        val subjectText = VerticalLayout(
            H3("메일 제목"),
            subject
        )

        val sender = VerticalLayout(H4("보낸사람"), createSender())
        val recipientFilter = VerticalLayout(H4("받는사람"), createRecipientFilter())

        val mailBody = VerticalLayout(
            content
        )

        val uploadFile = createUploadButton("첨부파일", MultiFileMemoryBuffer()) {
            /*
            todo: 추후 업로드 된 파일을 메일로 첨부하는 로직이 추가되어야 함
             (uploadFiles 같은 필드를 두고 mail을 보내는 기능에 포함시키면 될 것 같음)
            it.files.forEach { fileName ->
                val fileData = it.getFileData(fileName)
                val inputStream = it.getInputStream(fileName)
                val readBytes = inputStream.readBytes()
            }
            */
        }

        val sendButton = createMailSendButton()

        return VerticalLayout(
            subjectText,
            sender,
            recipientFilter,
            VerticalLayout(mailTargetGrid),
            mailBody,
            VerticalLayout(uploadFile),
            sendButton
        ).apply {
            setSizeFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
            defaultHorizontalComponentAlignment = FlexComponent.Alignment.CENTER
        }
    }

    private fun createRecipientFilter(): Component {
        return HorizontalLayout(
            createSearchBar(labelText = "받는사람") {
                if (it.isNotBlank()) {
                    addRecipientComponent(it)
                }
            },
            createSearchTargetComponent(),
            createGroupMailTargetComponent()
        )
    }

    private fun createDirectInsertTargetComponent(eventListener: (name: String) -> Unit): Div {
        val mailTarget = TextField().apply {
            addKeyDownListener(
                Key.ENTER,
                {
                    eventListener(this.value)
                    this.value = ""
                }
            )
        }

        return Div(
            HorizontalLayout(
                mailTarget,
                Button(Icon(VaadinIcon.ENTER_ARROW)) {
                    eventListener(mailTarget.value)
                    mailTarget.value = ""
                }
            )
        )
    }

    private fun createSearchTargetComponent(): Button {
        return createNormalButton("불러오기") {
            IndividualMailTargetFormDialog(applicantService, recipients) {
                recipients.addAll(it)
                mailTargetGrid.setItems(recipients.map { email -> MailTargetResponse(email) })
            }
        }.apply { isEnabled = true }
    }

    private fun createGroupMailTargetComponent(): Component {
        return createNormalButton("그룹 불러오기") {
            GroupMailTargetFormDialog(recruitmentService, evaluationService, mailTargetService) { targets ->
                targets.filterNot { recipients.contains(it) }
                    .forEach { addRecipientComponent(it) }
            }.apply { isEnabled = true }
        }
    }

    private fun createSender(): Component {
        val sender = TextField().apply {
            value = senderEmail
            isReadOnly = true
            style.set("background-color", "#959EA2")
        }
        return HorizontalLayout(sender)
    }

    private fun createMailSendButton(): VerticalLayout {
        return VerticalLayout(
            createPrimaryButton("전송") {
                // todo: emailService.메일전송(subject.value, recipients, body.value)
            }
        ).apply {
            setSizeFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
            defaultHorizontalComponentAlignment = FlexComponent.Alignment.CENTER
        }
    }

    private fun addRecipientComponent(email: String) {
        this.mailTargetGrid.apply {
            recipients.add(email)
            setItems(MailTargetResponse(email))
        }
    }

    private fun removeRecipientComponent(email: String) {
        recipients.remove(email)
        addAllCurrentRecipientComponent()
    }

    private fun addAllCurrentRecipientComponent() {
        // this.currentRecipients.removeAll()
        mailTargetGrid.removeAllColumns()
        this.mailTargetGrid.apply {
            setItems(recipients.map { MailTargetResponse(it) })
        }
    }

    private fun createMailBody(): TextArea {
        return TextArea("메일 본문").apply {
            setSizeFull()
            style.set("minHeight", "400px")
            placeholder = "메일 본문 입력"
        }
    }

    override fun bindOrNull(): MailSendData? {
        return bindDefaultOrNull()?.apply {
            targetMails = recipients
        }
    }

    override fun fill(data: MailSendData) {
        fillDefault(data)
    }
}
