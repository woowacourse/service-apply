package apply.ui.admin.selections

import apply.application.AssignmentService
import apply.application.EvaluationSelectData
import apply.application.EvaluationService
import apply.application.EvaluationTargetService
import apply.application.JudgmentService
import apply.application.MyMissionService
import apply.application.RecruitmentService
import apply.ui.admin.BaseLayout
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.tabs.Tab
import com.vaadin.flow.component.tabs.Tabs
import com.vaadin.flow.component.upload.Upload
import com.vaadin.flow.component.upload.receivers.MemoryBuffer
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.data.renderer.Renderer
import com.vaadin.flow.router.BeforeEvent
import com.vaadin.flow.router.HasUrlParameter
import com.vaadin.flow.router.Route
import com.vaadin.flow.router.WildcardParameter
import support.views.addSortableColumn
import support.views.createCsvUpload
import support.views.createErrorButton
import support.views.createNormalButton
import support.views.createPrimarySmallButton
import support.views.createSearchBox
import support.views.createSuccessButton

data class MyEvaluationTargetResponse(
    val id: Long,
    val name: String,
    val email: String,
    val totalScore: Double,
    val totalStatus: String,
    val myStatus: String
)

data class SelectionMeViewModel(
    val title: String,
    val evaluations: List<EvaluationSelectData>,
    val contents: List<MyEvaluationTargetResponse>
)

@Route(value = "admin/selections/me", layout = BaseLayout::class)
class SelectionMeView(
    private val recruitmentService: RecruitmentService,
    private val evaluationService: EvaluationService,
    private val evaluationTargetService: EvaluationTargetService,
    private val assignmentService: AssignmentService,
    private val myMissionService: MyMissionService,
    private val judgmentService: JudgmentService
) : VerticalLayout(), HasUrlParameter<Long> {
    private var recruitmentId: Long = 0L
    private lateinit var tabs: Tabs
    private var selectedTabIndex: Int = 0
    private lateinit var viewModel: SelectionMeViewModel

    override fun setParameter(event: BeforeEvent, @WildcardParameter parameter: Long) {
        recruitmentId = parameter
        viewModel = SelectionMeViewModel(
            title = recruitmentService.getById(recruitmentId).title,
            evaluations = evaluationService.getAllSelectDataByRecruitmentId(recruitmentId),
            contents = listOf(
                MyEvaluationTargetResponse(1, "홍길동1", "test1@test.com", 7.0, "평가 전", "평가 전"),
                MyEvaluationTargetResponse(2, "홍길동2", "test2@test.com", 7.8, "합격", "합격"),
                MyEvaluationTargetResponse(3, "홍길동3", "test3@test.com", 9.1, "합격", "탈락")
            )
        )

        add(createTitle(), createEvaluationTargetTabs(), createMenu(), createContent(), createEvaluationTargetFileButtons())
    }

    private fun createTitle(): Component {
        return HorizontalLayout(H1(viewModel.title)).apply {
            setWidthFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        }
    }

    private fun createEvaluationTargetTabs(): Component {
        return HorizontalLayout(
            createNormalButton("배정된 평가 대상자") { },
            createNormalButton("전체 평가 대상자") {
                UI.getCurrent().navigate(SelectionView::class.java, recruitmentId)
            }
        ).apply {
            setWidthFull()
            justifyContentMode = FlexComponent.JustifyContentMode.START
        }
    }

    private fun createMenu(): Component {
        val searchBox = createSearchBox()
        createTabs()
        val evaluationTargetAutoAssignmentButton = createEvaluationTargetAutoAssignmentButton()

        return HorizontalLayout(
            searchBox,
            tabs,
            evaluationTargetAutoAssignmentButton
        ).apply {
            setWidthFull()
            justifyContentMode = FlexComponent.JustifyContentMode.BETWEEN
        }
    }

    private fun createSearchBox(): Component {
        return HorizontalLayout(
            createSearchBox {
                // TODO 검색
            }
        )
    }

    private fun createEvaluationTargetAutoAssignmentButton(): Component {
        return HorizontalLayout(
            createErrorButton("평가 대상자 자동 배정") {
                // TODO 평가 대상자 자동 배정
            }
        )
    }

    private fun createTabs(): Component {
        tabs = Tabs(*viewModel.evaluations.map { Tab(it.title) }.toTypedArray()).apply {
            setWidthFull()
            addSelectedChangeListener {
                selectedTabIndex = selectedIndex
            }
            // TODO 탭 이동 (addSelectedChangeListener)
        }
        return tabs
    }

    private fun createContent(): Component {
        return Grid<MyEvaluationTargetResponse>(10).apply {
            addSortableColumn("이름", MyEvaluationTargetResponse::name)
            addSortableColumn("이메일", MyEvaluationTargetResponse::email)
            addSortableColumn("종합 점수", MyEvaluationTargetResponse::totalScore)
            addSortableColumn("종합 평가 상태", MyEvaluationTargetResponse::totalStatus)
            addSortableColumn("나의 평가 상태", MyEvaluationTargetResponse::myStatus)
            addColumn(createEvaluationButtonRenderer()).apply {
                isAutoWidth = true
                setHeader("평가")
            }
            setItems(viewModel.contents)
        }
    }

    private fun createEvaluationButtonRenderer(): Renderer<MyEvaluationTargetResponse> {
        return ComponentRenderer { response ->
            createPrimarySmallButton("평가하기") {
                EvaluationTargetFormDialog2(
                    evaluationTargetService,
                    assignmentService,
                    myMissionService,
                    judgmentService,
                    response.id
                ) {
                    removeAll()
                    add(createTitle(), createEvaluationTargetTabs(), createMenu(), createContent(), createEvaluationTargetFileButtons())
                }
            }
        }
    }

    private fun createEvaluationTargetFileButtons(): Component {
        return HorizontalLayout(
            createEvaluationFileDownloadButton(),
            createEvaluationFileUpload()
        ).apply {
            setWidthFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
            defaultVerticalComponentAlignment = FlexComponent.Alignment.CENTER
        }
    }

    private fun createEvaluationFileDownloadButton(): Button {
        return createSuccessButton("평가지 다운로드") {
            // TODO 평가지 다운로드
        }
    }

    private fun createEvaluationFileUpload(): Upload {
        return createCsvUpload("평가지 업로드", MemoryBuffer()) {
            // TODO 평가지 업로드
        }
    }
}
