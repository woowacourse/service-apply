package apply.ui.admin.selections

import apply.application.RecruitmentService
import apply.ui.admin.BaseLayout
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.BeforeEvent
import com.vaadin.flow.router.HasUrlParameter
import com.vaadin.flow.router.Route
import com.vaadin.flow.router.WildcardParameter
import support.views.createNormalButton

@Route(value = "admin/selections/me", layout = BaseLayout::class)
class SelectionMeView(
    private val recruitmentService: RecruitmentService
) : VerticalLayout(), HasUrlParameter<Long> {
    private var recruitmentId: Long = 0L
    private val modelView: MutableMap<String, Any> = mutableMapOf()

    override fun setParameter(event: BeforeEvent, @WildcardParameter parameter: Long) {
        this.recruitmentId = parameter
        modelView["title"] = recruitmentService.getById(recruitmentId).title

        add(createTitle(), createEvaluationTargetTabs(), createContent())
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

    private fun createContent(): Component {
        return HorizontalLayout().apply {
            setWidthFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        }
    }
}
