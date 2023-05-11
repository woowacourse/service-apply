package apply.ui.admin.selections

import apply.application.EvaluationSelectData
import apply.application.EvaluationService
import apply.application.RecruitmentService
import apply.ui.admin.BaseLayout
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.tabs.Tab
import com.vaadin.flow.component.tabs.Tabs
import com.vaadin.flow.router.BeforeEvent
import com.vaadin.flow.router.HasUrlParameter
import com.vaadin.flow.router.Route
import com.vaadin.flow.router.WildcardParameter
import support.views.createErrorButton
import support.views.createNormalButton
import support.views.createSearchBox

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
        add(createTitle(), createEvaluationTargetTabs(), createMenu(), createContent())
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
        return HorizontalLayout().apply {
            setWidthFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        }
    }
}
