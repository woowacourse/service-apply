package apply.ui.admin.mail

import apply.application.EvaluationService
import apply.application.MailTargetResponse
import apply.application.MailTargetService
import apply.application.RecruitmentResponse
import apply.application.RecruitmentService
import apply.domain.evaluation.Evaluation
import apply.domain.evaluationtarget.EvaluationStatus
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.select.Select
import support.views.addSortableColumn
import support.views.createErrorButton
import support.views.createItemSelect
import support.views.createPrimaryButton

class GroupMailTargetFormDialog(
    private val recruitmentService: RecruitmentService,
    private val evaluationService: EvaluationService,
    private val mailTargetService: MailTargetService,
    private val reloadComponent: (List<MailTargetResponse>) -> Unit
) : Dialog() {
    private val evaluation = createEvaluationItem()
    private val recruitment = createRecruitmentItem(evaluation)
    private val evaluationStatus = createEvaluationStatusItem(evaluation)
    private val mailTargets: MutableList<MailTargetResponse> = mutableListOf()
    private val currentMailTargets: VerticalLayout = VerticalLayout(createContent())

    init {
        width = "800px"
        height = "90%"
        add(
            H2("지원자 정보 조회"),
            HorizontalLayout(
                recruitment, evaluation, evaluationStatus,
                HorizontalLayout(
                    createPrimaryButton("추가") {
                        reloadComponent(mailTargets)
                        close()
                    },
                    createErrorButton("취소") {
                        close()
                    }
                ).apply {
                    justifyContentMode = FlexComponent.JustifyContentMode.END
                    defaultVerticalComponentAlignment = FlexComponent.Alignment.END
                },
            ),
            currentMailTargets
        ).apply {
            setWidthFull()
        }
        open()
    }

    private fun createEvaluationItem(): Select<Evaluation> {
        return createItemSelect("평가")
    }

    private fun createRecruitmentItem(evaluation: Select<Evaluation>): Select<RecruitmentResponse> {
        return createItemSelect<RecruitmentResponse>("모집").apply {
            setItems(*recruitmentService.findAll().toTypedArray())
            setItemLabelGenerator { it.title }
            addValueChangeListener {
                evaluation.apply {
                    setItems(*evaluationService.findAllByRecruitmentId(it.value.id).toTypedArray())
                    setItemLabelGenerator { it.title }
                }
            }
        }
    }

    private fun createEvaluationStatusItem(
        evaluation: Select<Evaluation>,
    ): Select<EvaluationStatus> {
        return createItemSelect<EvaluationStatus>("모집 상태").apply {
            setItems(*EvaluationStatus.values())
            setItemLabelGenerator { it.toText() }
            addValueChangeListener {
                val mailTargetResponses = mailTargetService.findMailTargets(evaluation.value.id, it.value)
                mailTargets.clear()
                mailTargets.addAll(mailTargetResponses)

                currentMailTargets.apply {
                    this.removeAll()
                    this.add(createContent(mailTargetResponses))
                }
            }
        }
    }

    private fun EvaluationStatus.toText() =
        when (this) {
            EvaluationStatus.WAITING -> "평가 전"
            EvaluationStatus.PASS -> "합격"
            EvaluationStatus.FAIL -> "탈락"
            EvaluationStatus.PENDING -> "보류"
        }

    private fun createContent(mailTargets: List<MailTargetResponse> = emptyList()): Component {
        val grids = createMailTargetsGrid(mailTargets)
        return VerticalLayout(grids).apply { setWidthFull() }
    }

    private fun createMailTargetsGrid(mailTargets: List<MailTargetResponse>): Component {
        return Grid<MailTargetResponse>(10).apply {
            addSortableColumn("이메일", MailTargetResponse::email)
            setItems(mailTargets)
        }
    }
}
