package apply.ui.admin.selections

import apply.application.AssignmentService
import apply.application.EvaluationTargetData
import apply.application.EvaluationTargetService
import apply.application.JudgmentData
import apply.application.JudgmentService
import apply.domain.judgment.JudgmentStatus
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
    private val judgmentItemId: Long?,
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
        return if (judgmentItemId != null) {
            val judgmentData = JudgmentData(
                "642951e1324eaf66914bd53df339d94cad5667e3",
                JudgmentStatus.STARTED,
                9,
                10,
                """
                    동해 물과 백두산이 마르고 닳도록
                    하느님이 보우하사 우리나라 만세
                    무궁화 삼천리 화려강산
                    대한 사람 대한으로 길이 보전하세
                """.trimIndent(),
                1L
            ) // TODO: 조회 기능 구현
            JudgmentForm(judgmentData, judgmentService)
        } else {
            FormLayout()
        }
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
