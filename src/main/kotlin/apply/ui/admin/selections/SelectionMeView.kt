package apply.ui.admin.selections

import apply.application.EvaluationSelectData
import apply.application.EvaluationService
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
    val name: String,
    val email: String,
    val totalScore: Double,
    val totalStatus: String,
    val myStatus: String
)

@Route(value = "admin/selections/me", layout = BaseLayout::class)
class SelectionMeView(
    private val recruitmentService: RecruitmentService,
    private val evaluationService: EvaluationService
) : VerticalLayout(), HasUrlParameter<Long> {
    private var recruitmentId: Long = 0L
    private val modelView: MutableMap<String, Any> = mutableMapOf()

    override fun setParameter(event: BeforeEvent, @WildcardParameter parameter: Long) {
        recruitmentId = parameter
        modelView["title"] = recruitmentService.getById(recruitmentId).title
        modelView["evaluations"] = evaluationService.getAllSelectDataByRecruitmentId(recruitmentId)
        modelView["contents"] = listOf(
            MyEvaluationTargetResponse("홍길동1", "test1@test.com", 7.0, "평가 전", "평가 전"),
            MyEvaluationTargetResponse("홍길동2", "test2@test.com", 7.8, "합격", "합격"),
            MyEvaluationTargetResponse("홍길동3", "test3@test.com", 9.1, "합격", "탈락")
        )

        add(createTitle(), createEvaluationTargetTabs(), createMenu(), createContent(), createEvaluationTargetFileButtons())
    }

    private fun createTitle(): Component {
        return HorizontalLayout(H1(modelView["title"] as String)).apply {
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
        val searchBox = HorizontalLayout(
            createSearchBox {
                // TODO
            }
        ).apply {
            justifyContentMode = FlexComponent.JustifyContentMode.START
        }
        val tabs = createTabs()
        val evaluationTargetAutoAssignmentButton = createErrorButton("평가 대상자 자동 배정") {
        }

        // TODO UI 정렬 맞추기
        return HorizontalLayout(
            searchBox,
            tabs,
            evaluationTargetAutoAssignmentButton
        ).apply {
            setWidthFull()
            justifyContentMode = FlexComponent.JustifyContentMode.BETWEEN
        }
    }

    private fun createTabs(): Component {
        val evaluations: List<EvaluationSelectData> = modelView["evaluations"] as List<EvaluationSelectData>
        return Tabs(*evaluations.map { Tab(it.title) }.toTypedArray())
    }

    private fun createContent(): Component {
        val contents = modelView["contents"] as List<MyEvaluationTargetResponse>
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
            setItems(contents)
        }
    }

    private fun createEvaluationButtonRenderer(): Renderer<MyEvaluationTargetResponse> {
        return ComponentRenderer { response ->
            createPrimarySmallButton("평가하기") {
                // TODO 평가하기
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
        }
    }

    private fun createEvaluationFileUpload(): Upload {
        return createCsvUpload("평가지 업로드", MemoryBuffer()) {
        }
    }
}
