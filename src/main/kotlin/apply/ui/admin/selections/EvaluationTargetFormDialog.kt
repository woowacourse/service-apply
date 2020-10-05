package apply.ui.admin.selections

import apply.application.EvaluationTargetData
import apply.application.EvaluationTargetService
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextArea
import support.views.BindingFormLayout
import support.views.createContrastButton
import support.views.createPrimaryButton

class EvaluationTargetFormDialog(
    private val evaluationTargetService: EvaluationTargetService,
    private val evaluationTargetId: Long
) : VerticalLayout() {
    private val title: H2 = H2().apply { alignItems = FlexComponent.Alignment.CENTER }
    private val description: TextArea = TextArea().apply {
        setWidthFull()
        isReadOnly = true
    }
    private val evaluationTargetForm: BindingFormLayout<EvaluationTargetData>
    private val dialog: Dialog

    init {
        val response = evaluationTargetService.getGradeEvaluation(evaluationTargetId)
        evaluationTargetForm = EvaluationTargetForm(response.evaluationItems).apply {
            fill(response.evaluationTargetData)
        }
        title.text = response.title
        description.value = response.description
        dialog = createDialog()
    }

    private fun createDialog(): Dialog {
        return Dialog().apply {
            add(createHeader(), evaluationTargetForm, createButtons())
            width = "800px"
            height = "90%"
            open()
        }
    }

    private fun createHeader(): VerticalLayout {
        return VerticalLayout(title, description).apply {
            alignItems = FlexComponent.Alignment.CENTER
        }
    }

    private fun createButtons(): Component {
        return HorizontalLayout(createAddButton(), createCancelButton()).apply {
            setSizeFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        }
    }

    private fun createAddButton(): Button {
        return createPrimaryButton("저장") {
            evaluationTargetForm.bindOrNull()?.let {
                evaluationTargetService.grade(evaluationTargetId, it)
                dialog.close()
            }
        }
    }

    private fun createCancelButton(): Button {
        return createContrastButton("취소") {
            dialog.close()
        }
    }
}
