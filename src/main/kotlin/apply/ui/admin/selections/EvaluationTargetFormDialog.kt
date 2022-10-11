package apply.ui.admin.selections

import apply.application.AssignmentService
import apply.application.EvaluationTargetData
import apply.application.EvaluationTargetService
import apply.application.JudgmentService
import apply.domain.judgment.JudgmentType
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.formlayout.FormLayout
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
    private val assignmentService: AssignmentService,
    private val judgmentService: JudgmentService,
    private val evaluationTargetId: Long,
    evaluationItemId: Long?,
    reloadComponents: () -> Unit
) : Dialog() {
    private val title: H2 = H2()
    private val description: TextArea = TextArea().apply {
        setWidthFull()
        isReadOnly = true
    }
    private val evaluationTargetForm: BindingFormLayout<EvaluationTargetData>

    init {
        val evaluationResponse = evaluationTargetService.getGradeEvaluation(evaluationTargetId)
        evaluationTargetForm = EvaluationTargetForm(evaluationResponse.evaluationItems, evaluationItemId)
            .apply { fill(evaluationResponse.evaluationTarget) }
        title.text = evaluationResponse.title
        description.value = evaluationResponse.description
        add(
            createHeader(),
            createAssignmentForm(),
            createJudgmentForm(),
            evaluationTargetForm,
            createButtons(reloadComponents)
        )
        width = "800px"
        open()
    }

    private fun createHeader(): VerticalLayout {
        return VerticalLayout(title, description).apply {
            alignItems = FlexComponent.Alignment.CENTER
            isPadding = false
            element.style.set("margin-bottom", "20px")
        }
    }

    private fun createAssignmentForm(): Component {
        return assignmentService.findByEvaluationTargetId(evaluationTargetId)
            ?.let { AssignmentForm(it) }
            ?: FormLayout()
    }

    private fun createJudgmentForm(): Component {
        return judgmentService.findByEvaluationTargetId(evaluationTargetId, JudgmentType.REAL)
            ?.let { JudgmentForm(it, judgmentService) }
            ?: FormLayout()
    }

    private fun createButtons(reloadComponent: () -> Unit): Component {
        return HorizontalLayout(getCreateAddButton(reloadComponent), createCancelButton()).apply {
            setWidthFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
            element.style.set("margin-top", "20px")
        }
    }

    private fun getCreateAddButton(reloadComponent: () -> Unit): Button {
        return createPrimaryButton("저장") {
            evaluationTargetForm.bindOrNull()?.let {
                evaluationTargetService.grade(evaluationTargetId, it)
                reloadComponent()
                close()
            }
        }
    }

    private fun createCancelButton(): Button {
        return createContrastButton("취소") {
            close()
        }
    }
}
