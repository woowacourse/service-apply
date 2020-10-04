package apply.ui.admin.selections

import apply.application.ApplicantResponse
import apply.application.ApplicantService
import apply.application.DownloadService
import apply.application.EvaluationTargetService
import apply.application.RecruitmentItemService
import apply.application.RecruitmentService
import apply.domain.applicationform.ApplicationForm
import apply.ui.admin.BaseLayout
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.html.H4
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.data.renderer.Renderer
import com.vaadin.flow.router.BeforeEvent
import com.vaadin.flow.router.HasUrlParameter
import com.vaadin.flow.router.Route
import com.vaadin.flow.router.WildcardParameter
import com.vaadin.flow.server.StreamResource
import com.vaadin.flow.server.VaadinSession
import support.views.addSortableColumn
import support.views.addSortableDateColumn
import support.views.addSortableDateTimeColumn
import support.views.createNormalButton
import support.views.createPrimarySmallButton
import support.views.createSearchBar
import support.views.createSuccessButton

@Route(value = "admin/selections", layout = BaseLayout::class)
class SelectionView(
    private val applicantService: ApplicantService,
    private val recruitmentService: RecruitmentService,
    private val recruitmentItemService: RecruitmentItemService,
    private val downloadService: DownloadService,
    private val evaluationTargetService: EvaluationTargetService
) : VerticalLayout(), HasUrlParameter<Long> {
    private var recruitmentId: Long = 0L

    private fun createTitle(): Component {
        return HorizontalLayout(H1(recruitmentService.getById(recruitmentId).title)).apply {
            setWidthFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        }
    }

    private fun createMenu(): Component {
        return HorizontalLayout(
            createSearchBar {
                removeAll()
                add(
                    createTitle(),
                    createMenu(),
                    createGrid(applicantService.findByRecruitmentIdAndKeyword(recruitmentId, it))
                )
            },
            createSuccessButton("다운로드") {
                val excel = { downloadService.createExcelBy(recruitmentId) }
                val registration = VaadinSession.getCurrent()
                    .resourceRegistry
                    .registerResource(StreamResource("${recruitmentService.getById(recruitmentId).title}.xlsx", excel))
                UI.getCurrent().page.setLocation(registration.resourceUri)
            }
        ).apply {
            setWidthFull()
            isSpacing = false
            justifyContentMode = FlexComponent.JustifyContentMode.BETWEEN
        }
    }

    private fun createGrid(applicants: List<ApplicantResponse>): Component {
        return Grid<ApplicantResponse>(10).apply {
            addSortableColumn("이름", ApplicantResponse::name)
            addSortableColumn("이메일", ApplicantResponse::email)
            addSortableColumn("전화번호", ApplicantResponse::phoneNumber)
            addSortableColumn("성별") { it.gender.title }
            addSortableDateColumn("생년월일", ApplicantResponse::birthday)
            addSortableDateTimeColumn("지원 일시") {
                it.applicationForm.submittedDateTime
            }
            addSortableColumn("부정 행위자") { if (it.isCheater) "O" else "X" }
            addColumn(createButtonRenderer()).apply { isAutoWidth = true }
            // TODO: 버튼 컴포넌트 위치 옮기기
            addColumn(createEvaluationButtonRenderer()).apply { isAutoWidth = true }
            setItems(applicants)
        }
    }

    private fun createButtonRenderer(): Renderer<ApplicantResponse> {
        return ComponentRenderer<Component, ApplicantResponse> { applicant ->
            createPrimarySmallButton("지원서") {
                val dialog = Dialog()
                dialog.add(*createRecruitmentItems(applicant.applicationForm))
                dialog.width = "800px"
                dialog.height = "90%"
                dialog.open()
            }
        }
    }

    private fun createRecruitmentItems(applicationForm: ApplicationForm): Array<Component> {
        val answers = applicationForm.answers
            .items
            .map { it.recruitmentItemId to it.contents }
            .toMap()
        val items = recruitmentItemService.findByRecruitmentIdOrderByPosition(recruitmentId)
            .map {
                createItem(it.title, createAnswer(answers.getValue(it.id)))
            }.toTypedArray()
        return addIfExist(items, applicationForm.referenceUrl)
    }

    private fun addIfExist(items: Array<Component>, referenceUrl: String): Array<Component> {
        return when {
            referenceUrl.isNotEmpty() -> {
                val referenceItem = createItem(
                    "포트폴리오",
                    createNormalButton(referenceUrl) {
                        UI.getCurrent().page.open(referenceUrl)
                    }
                )
                items.plusElement(referenceItem)
            }
            else -> items
        }
    }

    private fun createItem(title: String, component: Component): Component {
        return Div(H4(title), component).apply {
            setWidthFull()
            justifyContentMode = FlexComponent.JustifyContentMode.START
        }
    }

    private fun createAnswer(answer: String): Component {
        return TextArea().apply {
            setWidthFull()
            isReadOnly = true
            value = answer
            justifyContentMode = FlexComponent.JustifyContentMode.START
        }
    }

    override fun setParameter(event: BeforeEvent, @WildcardParameter parameter: Long) {
        this.recruitmentId = parameter
        add(
            createTitle(),
            createMenu(),
            createGrid(applicantService.findAllByRecruitmentId(recruitmentId))
        )
    }

    private fun createEvaluationButtonRenderer(): Renderer<ApplicantResponse> {
        return ComponentRenderer<Component, ApplicantResponse> { _ ->
            // TODO: Evaluation Target id 받아오기
            createPrimarySmallButton("평가하기") { GradeEvaluationFormDialog(evaluationTargetService, 1L) }
        }
    }
}
