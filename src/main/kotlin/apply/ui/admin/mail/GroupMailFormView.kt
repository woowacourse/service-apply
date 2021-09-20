package apply.ui.admin.mail

import apply.application.EvaluationService
import apply.application.MailTargetService
import apply.application.RecruitmentResponse
import apply.application.RecruitmentService
import apply.domain.evaluation.Evaluation
import apply.domain.evaluationtarget.EvaluationStatus
import apply.ui.admin.BaseLayout
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.router.Route
import org.springframework.beans.factory.annotation.Value
import support.views.Title
import support.views.createItemSelect

@Route(value = "group", layout = BaseLayout::class)
class GroupMailFormView(
    private val recruitmentService: RecruitmentService,
    private val evaluationService: EvaluationService,
    private val mailTargetService: MailTargetService,
    @Value("\${spring.mail.username}")
    private val senderEmail: String
) : MailFormView(senderEmail) {
    private val recruitment: Select<RecruitmentResponse>
    private val evaluation: Select<Evaluation>
    private val evaluationStatus: Select<EvaluationStatus>

    init {
        evaluation = createEvaluationItem()
        evaluationStatus = createEvaluationStatusItem(evaluation)
        recruitment = createRecruitmentItem(evaluation)
        add(Title("그룹 발송"), createMailForm())
        setWidthFull()
    }

    override fun createRecipientFilter(): Component {
        return HorizontalLayout(recruitment, evaluation, evaluationStatus)
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

    private fun createEvaluationItem(): Select<Evaluation> {
        return createItemSelect("평가")
    }

    private fun createEvaluationStatusItem(evaluation: Select<Evaluation>): Select<EvaluationStatus> {
        return createItemSelect<EvaluationStatus>("모집 상태").apply {
            setItems(*EvaluationStatus.values())
            setItemLabelGenerator { it.toText() }
            addValueChangeListener {
                clearCurrentRecipients()
                val mailTargets = mailTargetService.findMailTargets(evaluation.value.id, it.value).map { it.email }
                mailTargets.forEach { addRecipientComponent(it) }
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
}
