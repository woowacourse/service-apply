package apply.ui.admin.mail

import apply.application.EvaluationService
import apply.application.MailHistoryService
import apply.application.MailTargetResponse
import apply.application.MailTargetService
import apply.application.RecruitmentService
import apply.application.UserService
import apply.application.mail.MailData
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.Label
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.component.upload.Upload
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.data.renderer.Renderer
import org.springframework.boot.autoconfigure.mail.MailProperties
import support.views.BindingFormLayout
import support.views.NO_NAME
import support.views.addSortableColumn
import support.views.createErrorSmallButton
import support.views.createNormalButton
import support.views.createUpload

class MailForm(
    private val userService: UserService,
    private val recruitmentService: RecruitmentService,
    private val evaluationService: EvaluationService,
    private val mailTargetService: MailTargetService,
    private val mailHistoryService: MailHistoryService,
    private val mailProperties: MailProperties
) : BindingFormLayout<MailData>(MailData::class) {
    private val subject: TextField = TextField("제목").apply { setWidthFull() }
    private val body: TextArea = createBody()
    private val mailTargets: MutableSet<MailTargetResponse> = mutableSetOf()
    private val mailTargetsGrid: Grid<MailTargetResponse> = createMailTargetsGrid(mailTargets)
    private val mailTargetGridTitle: Label = Label()
    private val recipientFilter: Component = createRecipientFilter()
    private val fileUpload: Component = createFileUpload()

    init {
        add(subject, createSender(), recipientFilter, mailTargetGridTitle, mailTargetsGrid, body, fileUpload)
        setResponsiveSteps(ResponsiveStep("0", 1))
        drawRequired()
    }

    fun toReadOnlyMode() {
        this.subject.isReadOnly = true
        this.body.isReadOnly = true
        this.recipientFilter.isVisible = false
        this.mailTargetsGrid.getColumnByKey(DELETE_BUTTON).isVisible = false
        this.fileUpload.isVisible = false
    }

    private fun createSender(): Component {
        return TextField("보낸사람").apply {
            value = mailProperties.username
            isReadOnly = true
        }
    }

    private fun createRecipientFilter(): Component {
        return HorizontalLayout(
            createEnterBox(),
            createIndividualLoadButton(),
            createGroupLoadButton()
        ).apply { defaultVerticalComponentAlignment = FlexComponent.Alignment.END }
    }

    private fun createEnterBox(): Component {
        return support.views.createEnterBox(labelText = "받는사람 추가") {
            if (it.isNotBlank()) {
                mailTargets.addAndRefresh(MailTargetResponse(it, NO_NAME))
            }
        }
    }

    private fun createIndividualLoadButton(): Button {
        return createNormalButton("개별 불러오기") {
            IndividualMailTargetDialog(userService) {
                mailTargets.addAndRefresh(it)
            }
        }
    }

    private fun createGroupLoadButton(): Component {
        return createNormalButton("그룹 불러오기") {
            GroupMailTargetDialog(recruitmentService, evaluationService, mailTargetService) {
                mailTargets.addAllAndRefresh(it)
            }
        }
    }

    private fun setRowCount(count: Int) {
        mailTargetGridTitle.text = "받는사람: $count 명"
    }

    private fun createMailTargetsGrid(mailTargets: Set<MailTargetResponse>): Grid<MailTargetResponse> {
        return Grid<MailTargetResponse>(10).apply {
            addSortableColumn("이름") { it.name ?: NO_NAME }
            addSortableColumn("이메일", MailTargetResponse::email)
            addColumn(createRemoveButton()).key = DELETE_BUTTON
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
        return createUpload("파일첨부", MultiFileMemoryBuffer()) {
            // TODO: 추후 업로드 된 파일을 메일로 첨부하는 로직이 추가되어야 함
            // (uploadFiles 같은 필드를 두고 mail을 보내는 기능에 포함시키면 될 것 같음)
            // it.files.forEach { fileName ->
            //     val fileData = it.getFileData(fileName)
            //     val inputStream = it.getInputStream(fileName)
            //     val readBytes = inputStream.readBytes()
            // }
        }
    }

    private fun createRemoveButton(): Renderer<MailTargetResponse> {
        return ComponentRenderer<Component, MailTargetResponse> { response ->
            createErrorSmallButton("제거") {
                mailTargets.removeAndRefresh(response)
            }
        }
    }

    private fun MutableSet<MailTargetResponse>.addAndRefresh(element: MailTargetResponse) {
        add(element).also {
            mailTargetsGrid.dataProvider.refreshAll()
            setRowCount(mailTargets.size)
        }
    }

    private fun MutableSet<MailTargetResponse>.addAllAndRefresh(elements: Collection<MailTargetResponse>) {
        addAll(elements).also {
            mailTargetsGrid.dataProvider.refreshAll()
            setRowCount(mailTargets.size)
        }
    }

    private fun MutableSet<MailTargetResponse>.removeAndRefresh(element: MailTargetResponse) {
        remove(element).also {
            mailTargetsGrid.dataProvider.refreshAll()
            setRowCount(mailTargets.size)
        }
    }

    override fun bindOrNull(): MailData? {
        return bindDefaultOrNull()?.apply {
            recipients = mailTargets.map { it.email }.toList()
        }
    }

    override fun fill(data: MailData) {
        setRowCount(data.recipients.size)
        fillDefault(data)
        mailTargets.addAll(mailHistoryService.findAllMailTargetsByEmails(data.recipients))
    }

    companion object {
        private const val DELETE_BUTTON: String = "삭제버튼"
    }
}
