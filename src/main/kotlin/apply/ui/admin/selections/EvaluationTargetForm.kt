package apply.ui.admin.selections

import apply.application.EvaluationItemResponse
import apply.application.EvaluationTargetData
import apply.domain.evaluationtarget.EvaluationStatus
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.component.textfield.IntegerField
import com.vaadin.flow.component.textfield.TextArea
import support.views.BindingFormLayout

private const val FIXED_ADDED_COMPONENT_COUNT = 3

class EvaluationTargetForm() : BindingFormLayout<EvaluationTargetData>(EvaluationTargetData::class) {
    private val evaluationAnswers: MutableList<EvaluationAnswerForm> = mutableListOf()
    private val note: TextArea = TextArea("기타 특이사항")
    private val evaluationStatus: Select<EvaluationStatus> = Select(*EvaluationStatus.values()).apply {
        setItemLabelGenerator { it.toText() }
        label = "평가 상태"
    }
    private val sumOfScore: IntegerField = IntegerField("합계").apply { isReadOnly = true }

    init {
        add(note, evaluationStatus, sumOfScore)
        setColspan(note, 2)
        drawRequired()
    }

    constructor(evaluationItems: List<EvaluationItemResponse>) : this() {
        evaluationItems.forEach {
            val answerForm = EvaluationAnswerForm(it.title, it.description, it.maximumScore).apply {
                setColspan(this, 2)
            }
            evaluationAnswers.add(answerForm)
            addComponentAtIndex(getIndexOfLastAnswer(), answerForm)
        }
    }

    private fun sumOfScore() = evaluationAnswers.map { it.score.value }.sum()

    private fun EvaluationStatus.toText() =
        when (this) {
            EvaluationStatus.WAITING -> "평가 전"
            EvaluationStatus.PASS -> "합격"
            EvaluationStatus.FAIL -> "탈락"
            EvaluationStatus.PENDING -> "보류"
        }

    private fun getIndexOfLastAnswer() = (children.count() - FIXED_ADDED_COMPONENT_COUNT).toInt()

    override fun bindOrNull(): EvaluationTargetData? {
        val result = bindDefaultOrNull()
        val answers = evaluationAnswers.mapNotNull { it.bindOrNull() }
        if (evaluationAnswers.size != answers.size) {
            return null
        }
        return result?.apply { evaluationAnswersData = answers }
    }

    override fun fill(data: EvaluationTargetData) {
        fillDefault(data)
        (evaluationAnswers zip data.evaluationAnswersData).forEach {
            it.first.apply {
                fill(it.second)
                setScoreChangeEvent { sumOfScore.value = sumOfScore() }
            }
        }
        sumOfScore.value = sumOfScore()
    }
}
