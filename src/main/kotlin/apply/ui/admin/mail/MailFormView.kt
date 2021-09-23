package apply.ui.admin.mail

import apply.application.ApplicantService
import apply.application.EvaluationService
import apply.application.MailTargetResponse
import apply.application.MailTargetService
import apply.application.RecruitmentService
import apply.application.mail.MailSendData
import apply.ui.admin.BaseLayout
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.html.H3
import com.vaadin.flow.component.html.H4
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.data.renderer.Renderer
import com.vaadin.flow.router.Route
import org.springframework.boot.autoconfigure.mail.MailProperties
import support.views.BindingFormLayout
import support.views.addSortableColumn
import support.views.createNormalButton
import support.views.createPrimaryButton
import support.views.createPrimarySmallButton
import support.views.createSearchBar
import support.views.createUploadButton

@Route(value = "admin/emails", layout = BaseLayout::class)
class MailFormView(
    private val applicantService: ApplicantService,
    private val recruitmentService: RecruitmentService,
    private val evaluationService: EvaluationService,
    private val mailTargetService: MailTargetService,
    private val mailProperties: MailProperties
) : BindingFormLayout<MailSendData>(MailSendData::class) {
    private val subject: TextField = TextField()
    private val content: TextArea = createMailBody()
    private val mailTargetGrid: Grid<MailTargetResponse> = createMailTargetsGrid()
    private val mailTargets: MutableSet<MailTargetResponse> = mutableSetOf()

    init {
        add(createTitle(), createMailForm())
        setWidthFull()
        setResponsiveSteps(ResponsiveStep("0", 1))
        drawRequired()
    }

    private fun createTitle(): Component {
        return HorizontalLayout(H1("메일 발송")).apply {
            setSizeFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        }
    }

    private fun createMailForm(): Component {
        val subjectText = VerticalLayout(H3("메일 제목"), subject).apply { setWidthFull() }
        val sender = VerticalLayout(H4("보낸사람"), createSender())
        val recipientFilter = VerticalLayout(H4("받는사람"), createRecipientFilter())
        val mailBody = VerticalLayout(content)
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

    private fun createMailTargetsGrid(): Grid<MailTargetResponse> {
        return Grid<MailTargetResponse>(10).apply {
            addSortableColumn("이름", MailTargetResponse::name)
            addSortableColumn("이메일", MailTargetResponse::email)
            addColumn(createRemoveButtonRender())
        }
    }

    private fun createRemoveButtonRender(): Renderer<MailTargetResponse> {
        return ComponentRenderer<Component, MailTargetResponse> { response ->
            createPrimarySmallButton("삭제") {
                mailTargets.remove(response)
                mailTargetGrid.setItems(mailTargets)
            }
        }
    }

    private fun createRecipientFilter(): Component {
        return HorizontalLayout(
            createSearchBar(labelText = "받는사람") {
                if (it.isNotBlank()) {
                    mailTargets.add(MailTargetResponse("직접 입력", it))
                    mailTargetGrid.setItems(mailTargets)
                }
            },
            createSearchTargetComponent(),
            createGroupMailTargetComponent()
        ).apply { defaultVerticalComponentAlignment = FlexComponent.Alignment.END }
    }

    private fun createSearchTargetComponent(): Button {
        return createNormalButton("불러오기") {
            IndividualMailTargetFormDialog(applicantService) {
                mailTargets.add(it)
                mailTargetGrid.setItems(mailTargets)
            }
        }.apply { isEnabled = true }
    }

    private fun createGroupMailTargetComponent(): Component {
        return createNormalButton("그룹 불러오기") {
            GroupMailTargetFormDialog(recruitmentService, evaluationService, mailTargetService) {
                mailTargets.addAll(it)
                mailTargetGrid.setItems(mailTargets)
            }.apply { isEnabled = true }
        }
    }

    private fun createSender(): Component {
        val sender = TextField().apply {
            value = mailProperties.username
            isReadOnly = true
        }
        return HorizontalLayout(sender)
    }

    private fun createMailSendButton(): VerticalLayout {
        return VerticalLayout(
            createPrimaryButton("전송") {
                bindOrNull()?.let {
                    // todo: emailService.메일전송(it, uploadFile)
                }
            }
        ).apply {
            setSizeFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
            defaultHorizontalComponentAlignment = FlexComponent.Alignment.CENTER
        }
    }

    private fun createMailBody(): TextArea {
        return TextArea("메일 본문").apply {
            setSizeFull()
            style.set("minHeight", "400px")
        }
    }

    override fun bindOrNull(): MailSendData? {
        return bindDefaultOrNull()?.apply {
            targetMails = mailTargets.map { it.email }
                .toList()
        }
    }

    override fun fill(data: MailSendData) {
        fillDefault(data)
    }
}
