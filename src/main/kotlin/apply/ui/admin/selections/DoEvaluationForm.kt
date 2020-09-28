package apply.ui.admin.selections

import apply.application.DoEvaluationRequest
import apply.application.DoEvaluationResponse
import apply.domain.dummy.EvaluationStatus
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.component.textfield.TextField
import support.views.BindingFormLayout

class DoEvaluationForm() : BindingFormLayout<DoEvaluationRequest>(DoEvaluationRequest::class) {
    private val evaluateAnswers: MutableList<EvaluateAnswerForm> = mutableListOf()
    private val note: TextArea = TextArea("기타 특이사항")
    private val evaluationStatus: Select<EvaluationStatus> = Select(*EvaluationStatus.values()).apply {
        setItemLabelGenerator { it.label() }
        label = "평가 상태"
    }
    private val sumOfScore: TextField = TextField("합계 ").apply { isReadOnly = true }

    init {
        setColspan(note, 2)
        drawRequired()
    }

    constructor(doEvaluationResponse: DoEvaluationResponse) : this() {
        val items = doEvaluationResponse.evaluationItems.map {
            EvaluateAnswerForm(it, this::reCalculateTotalScore).apply { setColspan(this, 2) }
        }
        evaluateAnswers.addAll(items)
        evaluationStatus.value = doEvaluationResponse.evaluationStatus
        add(*items.toList().toTypedArray(), note, evaluationStatus, sumOfScore)
    }

    private fun EvaluationStatus.label() =
        when (this) {
            EvaluationStatus.WAITING -> "평가 전"
            EvaluationStatus.PASS -> "합격"
            EvaluationStatus.FAIL -> "탈락"
            EvaluationStatus.PENDING -> "보류"
        }

    private fun sumOfScore() = evaluateAnswers.map { it.score.value }.sum()

    private fun reCalculateTotalScore() {
        sumOfScore.value = sumOfScore().toString()
    }

    override fun bindOrNull(): DoEvaluationRequest? {
        val result = bindDefaultOrNull()
        val answers = evaluateAnswers.mapNotNull { it.bindOrNull() }
        if (evaluateAnswers.size != answers.size) {
            return null
        }
        return result?.apply { evaluationAnswers = answers }
    }
}
