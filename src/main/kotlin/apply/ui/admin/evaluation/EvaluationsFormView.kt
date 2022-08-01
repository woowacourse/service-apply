package apply.ui.admin.evaluation

import apply.application.EvaluationService
import apply.ui.admin.BaseLayout
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.BeforeEvent
import com.vaadin.flow.router.HasUrlParameter
import com.vaadin.flow.router.Route
import com.vaadin.flow.router.WildcardParameter
import support.views.EDIT_VALUE
import support.views.FORM_URL_PATTERN
import support.views.Title
import support.views.createContrastButton
import support.views.createPrimaryButton
import support.views.toDisplayName

@Route(value = "admin/evaluations", layout = BaseLayout::class)
class EvaluationsFormView(
    private val evaluationService: EvaluationService
) : VerticalLayout(), HasUrlParameter<String> {
    private val title: Title = Title()
    private val evaluationForm: EvaluationForm = EvaluationForm(evaluationService.findAllRecruitmentSelectData()) {
        evaluationService.getAllSelectDataByRecruitmentId(it)
    }
    private val submitButton: Button = createSubmitButton()

    init {
        add(title, evaluationForm, createButtons())
    }

    override fun setParameter(event: BeforeEvent, @WildcardParameter parameter: String) {
        val result = FORM_URL_PATTERN.find(parameter) ?: return UI.getCurrent().page.history.back()
        val (id, value) = result.destructured
        setDisplayName(value.toDisplayName())
        if (value == EDIT_VALUE) {
            evaluationForm.fill(evaluationService.getDataById(id.toLong()))
        }
    }

    private fun setDisplayName(displayName: String) {
        title.text = "평가 $displayName"
        submitButton.text = displayName
    }

    private fun createSubmitButton(): Button {
        return createPrimaryButton {
            evaluationForm.bindOrNull()?.let {
                evaluationService.save(it)
                UI.getCurrent().navigate(EvaluationsView::class.java)
            }
        }
    }

    private fun createButtons(): Component {
        return HorizontalLayout(submitButton, createCancelButton()).apply {
            setSizeFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        }
    }

    private fun createCancelButton(): Button {
        return createContrastButton("취소") {
            UI.getCurrent().navigate(EvaluationsView::class.java)
        }
    }
}
