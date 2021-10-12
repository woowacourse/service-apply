package apply.ui.admin.selections

import apply.application.AssignmentData
import apply.application.AssignmentService
import apply.application.EvaluationTargetData
import apply.application.EvaluationTargetService
import apply.domain.mission.Mission
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
    private val assignmentService: AssignmentService,
    private val mission: Mission?,
    private val evaluationTargetId: Long,
    reloadComponents: () -> Unit
) : Dialog() {
    private val title: H2 = H2()
    private val description: TextArea = TextArea().apply {
        setWidthFull()
        isReadOnly = true
    }
    private val assignmentForm: BindingFormLayout<AssignmentData>
    private val evaluationTargetForm: BindingFormLayout<EvaluationTargetData>

    init {
        assignmentForm = createAssignmentForm()
        val response = evaluationTargetService.getGradeEvaluation(evaluationTargetId)
        evaluationTargetForm = EvaluationTargetForm(response.evaluationItems)
            .apply { fill(response.evaluationTarget) }
        title.text = response.title
        description.value = response.description

        add(createHeader(), assignmentForm, evaluationTargetForm, createButtons(reloadComponents))
        width = "800px"
        height = "90%"
        open()
    }

    private fun createAssignmentForm(): AssignmentForm {
        return mission?.let {
            val assignment = assignmentService.findByEvaluationTargetIdAndMissionId(evaluationTargetId, it.id)
            AssignmentForm().apply {
                fill(assignment)
            }
        } ?: AssignmentForm().apply {
            isVisible = false
        }
    }

    private fun createHeader(): VerticalLayout {
        return VerticalLayout(title, description).apply {
            alignItems = FlexComponent.Alignment.CENTER
            isPadding = false
            element.style.set("margin-bottom", "20px")
        }
    }

    private fun createButtons(reloadComponent: () -> Unit): Component {
        return HorizontalLayout(getCreateAddButton(reloadComponent), createCancelButton()).apply {
            setSizeFull()
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
