package apply.ui.admin.mail

import apply.application.EvaluationService
import apply.application.MailTargetResponse
import apply.application.MailTargetService
import apply.application.MemberService
import apply.application.RecruitmentService
import apply.application.mail.MailData
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.component.upload.Upload
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.data.renderer.Renderer
import elemental.json.JsonObject
import org.springframework.boot.autoconfigure.mail.MailProperties
import org.springframework.core.io.ByteArrayResource
import support.views.BindingFormLayout
import support.views.NO_NAME
import support.views.addSortableColumn
import support.views.createErrorSmallButton
import support.views.createNormalButton
import support.views.createUpload

class MailForm(
    private val memberService: MemberService,
    private val recruitmentService: RecruitmentService,
    private val evaluationService: EvaluationService,
    private val mailTargetService: MailTargetService,
    private val mailProperties: MailProperties
) : BindingFormLayout<MailData>(MailData::class) {
    private val subject: TextField = TextField("제목").apply { setWidthFull() }
    private val sender: TextField = createSender()
    private val body: TextArea = createBody()
    private val mailTargets: MutableSet<MailTargetResponse> = mutableSetOf()
    private val uploadFile: MutableMap<String, ByteArrayResource> = mutableMapOf()
    private val mailTargetsGrid: Grid<MailTargetResponse> = createMailTargetsGrid(mailTargets)
    private val recipientFilter: Component = createRecipientFilter()
    private val fileUpload: Upload = createFileUpload()
    private var mailData: MailData? = null

    init {
        add(subject, sender, recipientFilter, mailTargetsGrid, body, fileUpload)
        setResponsiveSteps(ResponsiveStep("0", 1))
        drawRequired()
        refreshGridFooter()
    }

    private fun createSender(): TextField {
        return TextField("보낸사람").apply {
            value = mailProperties.username
            isReadOnly = true
        }
    }

    private fun createRecipientFilter(): Component {
        return HorizontalLayout(
            createIndividualLoadButton(),
            createGroupLoadButton()
        ).apply { defaultVerticalComponentAlignment = FlexComponent.Alignment.END }
    }

    private fun createIndividualLoadButton(): Button {
        return createNormalButton("개별 불러오기") {
            IndividualMailTargetDialog(memberService) {
                refreshGrid { mailTargets.add(it) }
            }
        }
    }

    private fun createGroupLoadButton(): Component {
        return createNormalButton("그룹 불러오기") {
            GroupMailTargetDialog(recruitmentService, evaluationService, mailTargetService) {
                refreshGrid { mailTargets.addAll(it) }
            }
        }
    }

    private fun createMailTargetsGrid(mailTargets: Set<MailTargetResponse>): Grid<MailTargetResponse> {
        return Grid<MailTargetResponse>(10).apply {
            addSortableColumn("이름") { it.name ?: NO_NAME }
            addSortableColumn("이메일", MailTargetResponse::email)
            addColumn(createRemoveButton())
            setItems(mailTargets)
        }
    }

    private fun createBody(): TextArea {
        return TextArea("본문").apply {
            setSizeFull()
            style.set("minHeight", "400px")
        }
    }

    private fun createFileUpload(): Upload {
        val upload = createUpload("파일첨부", MultiFileMemoryBuffer()) {
            it.files.forEach { fileName ->
                val byteArray = it.getInputStream(fileName).readBytes()
                uploadFile[fileName] = ByteArrayResource(byteArray)
            }
        }
        upload.element.addEventListener("file-remove") { event ->
            val eventData: JsonObject = event.eventData
            uploadFile.remove(eventData.getString("event.detail.file.name"))
        }.addEventData("event.detail.file.name")
        return upload
    }

    private fun createRemoveButton(): Renderer<MailTargetResponse> {
        return ComponentRenderer { response ->
            createErrorSmallButton("제거") {
                refreshGrid { mailTargets.remove(response) }
            }
        }
    }

    override fun bindOrNull(): MailData? {
        if (mailTargets.isEmpty()) {
            return null
        }
        return bindDefaultOrNull()?.apply {
            recipients = mailTargets.map { it.id }.toList()
            attachments = uploadFile
        }
    }

    override fun fill(data: MailData) {
        mailData = data
        fillDefault(data)
        toReadOnlyMode()
        refreshGrid { mailTargets.addAll(mailTargetService.findAllByMemberIds(data.recipients)) }
    }

    private fun toReadOnlyMode() {
        subject.isReadOnly = true
        body.isReadOnly = true
        mailTargetsGrid.columns.last().isVisible = false
        recipientFilter.isVisible = false
        fileUpload.isVisible = false
    }

    private fun refreshGrid(action: MutableSet<MailTargetResponse>.() -> Unit = {}) {
        mailTargets.action()
        mailTargetsGrid.dataProvider.refreshAll()
        refreshGridFooter()
    }

    private fun refreshGridFooter() {
        mailData?.let {
            mailTargetsGrid.columns.first().setFooter("받는사람: ${mailTargets.size}명 (탈퇴 회원 포함 총 ${it.recipients.size}명)")
        } ?: mailTargetsGrid.columns.first().setFooter("받는사람: ${mailTargets.size}명")
    }
}
