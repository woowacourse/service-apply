package apply.ui.admin.selections

import apply.application.GradeEvaluationRequest
import apply.application.GradeEvaluationResponse
import apply.domain.evaluationtarget.EvaluationStatus
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.component.textfield.IntegerField
import com.vaadin.flow.component.textfield.TextArea
import support.views.BindingFormLayout

class GradeEvaluationForm() :
    BindingFormLayout<GradeEvaluationRequest>(GradeEvaluationRequest::class) {

    private val evaluationAnswers: MutableList<EvaluationAnswerForm> = mutableListOf()
    private val note: TextArea = TextArea("기타 특이사항")
    private val evaluationStatus: Select<EvaluationStatus> = Select(*EvaluationStatus.values()).apply {
        setItemLabelGenerator { it.label() }
        label = "평가 상태"
    }
    private val sumOfScore: IntegerField = IntegerField("합계 ").apply { isReadOnly = true }

    init {
        setColspan(note, 2)
        drawRequired()
    }

    constructor(gradeEvaluationResponse: GradeEvaluationResponse) : this() {
        val items = gradeEvaluationResponse.evaluationItems.map {
            EvaluationAnswerForm(it) {
                sumOfScore.value = sumOfScore()
            }.apply { setColspan(this, 2) }
        }
        evaluationAnswers.addAll(items)
        evaluationStatus.value = gradeEvaluationResponse.evaluationStatus
        note.value = gradeEvaluationResponse.note
        sumOfScore.value = sumOfScore()
        add(*items.toList().toTypedArray(), note, evaluationStatus, sumOfScore)
    }

    private fun EvaluationStatus.label() =
        when (this) {
            EvaluationStatus.WAITING -> "평가 전"
            EvaluationStatus.PASS -> "합격"
            EvaluationStatus.FAIL -> "탈락"
            EvaluationStatus.PENDING -> "보류"
        }

    private fun sumOfScore() = evaluationAnswers.map { it.score.value }.sum()

    override fun bindOrNull(): GradeEvaluationRequest? {
        val result = bindDefaultOrNull()
        val answers = evaluationAnswers.mapNotNull { it.bindOrNull() }
        if (evaluationAnswers.size != answers.size) {
            return null
        }
        return result?.apply { evaluationAnswers = answers }
    }
}
