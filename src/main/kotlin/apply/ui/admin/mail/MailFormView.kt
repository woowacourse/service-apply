package apply.ui.admin.mail

import apply.application.ApplicantResponse
import apply.application.ApplicantService
import apply.application.EvaluationService
import apply.application.MailTargetService
import apply.application.RecruitmentResponse
import apply.application.RecruitmentService
import apply.domain.evaluation.Evaluation
import apply.domain.evaluationtarget.EvaluationStatus
import apply.ui.admin.BaseLayout
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.Key
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.html.H3
import com.vaadin.flow.component.html.H4
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.data.renderer.Renderer
import com.vaadin.flow.router.Route
import org.springframework.beans.factory.annotation.Value
import support.views.Title
import support.views.addSortableColumn
import support.views.createErrorButton
import support.views.createItemSelect
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
) : VerticalLayout() {
    private val subject: TextField = TextField("메일 제목", "메일 제목 입력")
    private val recipients: MutableList<String> = mutableListOf()
    private val body: TextArea = createMailBody()
    private val currentRecipients = HorizontalLayout()

    init {
        add(Title("메일 발송"), createMailForm())
        setWidthFull()
    }

    private fun createMailForm(): Component {
        val subjectText = VerticalLayout(
            H3("메일 제목"),
            subject.apply { setSizeFull() }
        )

        val sender = VerticalLayout(H4("보낸사람"), createSender())
        val recipientFilter = VerticalLayout(H4("받는사람"), createRecipientFilter())

        val mailBody = VerticalLayout(
            body
        ).apply {
            setSizeFull()
        }

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
            VerticalLayout(currentRecipients.apply { setWidthFull() }).apply { },
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
            createDirectInsertTargetComponent {
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
            Dialog().apply {
                width = "800px"
                height = "90%"
                add(
                    H2("지원자 정보 조회"),
                    HorizontalLayout(
                        createAddRecipients(),
                        HorizontalLayout(
                            createErrorButton("취소") {
                                close()
                            }
                        ).apply {
                            justifyContentMode = FlexComponent.JustifyContentMode.END
                            defaultHorizontalComponentAlignment = FlexComponent.Alignment.END
                        }
                    )
                ).apply {
                    setWidthFull()
                }
                open()
            }
        }.apply { isEnabled = true }
    }

    private fun createEvaluationItem(): Select<Evaluation> {
        return createItemSelect("평가")
    }

    private fun createEvaluationStatusItem(evaluation: Select<Evaluation>): Select<EvaluationStatus> {
        return createItemSelect<EvaluationStatus>("모집 상태").apply {
            setItems(*EvaluationStatus.values())
            setItemLabelGenerator { it.toText() }
            addValueChangeListener {
                val mailTargets = mailTargetService.findMailTargets(evaluation.value.id, it.value).map { it.email }
                mailTargets.filterNot { recipients.contains(it) }
                    .forEach { addRecipientComponent(it) }
            }
        }
    }

    private fun EvaluationStatus.toText() =
        when (this) {
            EvaluationStatus.WAITING -> "평가 전"
            EvaluationStatus.PASS -> "합격"
            EvaluationStatus.FAIL -> "탈락"
            EvaluationStatus.PENDING -> "보류"
        }

    private fun createGroupMailTargetComponent(): Component {
        val evaluation = createEvaluationItem()
        val recruitment = createRecruitmentItem(evaluation)
        val evaluationStatus = createEvaluationStatusItem(evaluation)

        return createNormalButton("그룹 불러오기") {
            Dialog().apply {
                width = "800px"
                height = "90%"
                add(
                    H2("지원자 정보 조회"),
                    HorizontalLayout(
                        recruitment, evaluation, evaluationStatus,
                        HorizontalLayout(
                            createErrorButton("취소") {
                                close()
                            }
                        ).apply {
                            justifyContentMode = FlexComponent.JustifyContentMode.END
                            defaultHorizontalComponentAlignment = FlexComponent.Alignment.END
                        }
                    )
                ).apply {
                    setWidthFull()
                }
                open()
            }
        }.apply { isEnabled = true }
    }

    private fun createAddRecipients(): Component {
        val container = VerticalLayout()
        return VerticalLayout(
            createSearchBar {
                container.removeAll()
                val founds = applicantService.findAllByKeyword(it)
                if (founds.isNotEmpty()) {
                    container.add(
                        createGrid(founds)
                    )
                }
            },
            container
        )
    }

    private fun createGrid(applicantResponse: List<ApplicantResponse>): Component {
        return Grid<ApplicantResponse>(10).apply {
            addSortableColumn("이름", ApplicantResponse::name)
            addSortableColumn("이메일", ApplicantResponse::email)
            addColumn(createEditAndDeleteButton()).apply { isAutoWidth = true }
            setItems(applicantResponse)
        }
    }

    private fun createEditAndDeleteButton(): Renderer<ApplicantResponse> {
        return ComponentRenderer<Component, ApplicantResponse> { applicantResponse ->
            HorizontalLayout(
                createAddOrDeleteButton(applicantResponse)
            )
        }
    }

    private fun createAddOrDeleteButton(applicantResponse: ApplicantResponse): Component {
        if (this.recipients.contains(applicantResponse.email)) {
            return createTargetDeleteButton(applicantResponse)
        }

        return createTargetAddButton(applicantResponse)
    }

    private fun createTargetAddButton(applicantResponse: ApplicantResponse): Button {
        return createPrimaryButton("추가") {
            addRecipientComponent(applicantResponse.email)
        }.apply {
            isDisableOnClick = true
        }
    }

    private fun createTargetDeleteButton(applicantResponse: ApplicantResponse): Button {
        return createErrorButton("삭제") {
            removeRecipientComponent(applicantResponse.email)
        }.apply {
            isDisableOnClick = true
        }
    }

    private fun createSender(): Component {
        val sender = TextField().apply {
            value = senderEmail
            isReadOnly = true
            style.set("background-color", "#00B493")
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
        this.currentRecipients.apply {
            recipients.add(email)
            add(createRecipientComponent(email))
        }
    }

    private fun removeRecipientComponent(email: String) {
        recipients.remove(email)
        addAllCurrentRecipientComponent()
    }

    private fun addAllCurrentRecipientComponent() {
        this.currentRecipients.removeAll()
        this.currentRecipients.apply {
            recipients.forEach {
                add(createRecipientComponent(it))
            }
        }
    }

    private fun createMailBody(): TextArea {
        return TextArea("메일 본문").apply {
            setSizeFull()
            style.set("minHeight", "400px")
            placeholder = "메일 본문 입력"
        }
    }

    private fun createRecipientComponent(email: String): Component {
        val emailTarget = TextField().apply {
            value = email
            isReadOnly = true
            style.set("background-color", "#00B493")
        }

        return Span(
            emailTarget,
            Button(Icon(VaadinIcon.CLOSE_SMALL)) {
                removeRecipientComponent(email)
            }
        )
    }

    private fun createRecruitmentItem(evaluation: Select<Evaluation>): Select<RecruitmentResponse> {
        return createItemSelect<RecruitmentResponse>("모집").apply {
            setItems(*recruitmentService.findAll().toTypedArray())
            setItemLabelGenerator { it.title }
            addValueChangeListener {
                evaluation.apply {
                    setItems(*evaluationService.findAllByRecruitmentId(it.value.id).toTypedArray())
                    setItemLabelGenerator { it.title }
                }
            }
        }
    }
}
