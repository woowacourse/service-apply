package apply.ui.admin.evaluation

import apply.application.EvaluationService
import apply.application.RecruitmentService
import apply.ui.admin.BaseLayout
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.Route
import support.views.createContrastButton
import support.views.createPrimaryButton

@Route(value = "admin/evaluations/form", layout = BaseLayout::class)
class EvaluationsFormView(
    private val evaluationService: EvaluationService,
    private val recruitmentService: RecruitmentService
) : VerticalLayout() {
    private val evaluationForm: EvaluationForm = EvaluationForm(recruitmentService.findAll()) {
        evaluationService.findAllByRecruitmentId(it)
    }

    init {
        add(createTitle(), evaluationForm, createButtons())
    }

    private fun createTitle(): Component {
        return HorizontalLayout(H1("평가 생성")).apply {
            setSizeFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        }
    }

    private fun createButtons(): Component {
        return HorizontalLayout(createAddButton(), createCancelButton()).apply {
            setSizeFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        }
    }

    private fun createAddButton(): Button {
        return createPrimaryButton("생성") {
            evaluationForm.bindOrNull()?.let {
                print(it.toString())
                evaluationService.save(it)
                UI.getCurrent().navigate(EvaluationsView::class.java)
            }
        }
    }

    private fun createCancelButton(): Button {
        return createContrastButton("취소") {
            UI.getCurrent().navigate(EvaluationsView::class.java)
        }
    }
}
