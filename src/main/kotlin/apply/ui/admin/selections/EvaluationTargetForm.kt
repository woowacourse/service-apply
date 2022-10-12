package apply.ui.admin.selections

import apply.application.EvaluationItemResponse
import apply.application.EvaluationTargetData
import apply.domain.evaluationtarget.EvaluationStatus
import com.vaadin.flow.component.html.H3
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.component.textfield.IntegerField
import com.vaadin.flow.component.textfield.TextArea
import support.views.BindingFormLayout
import support.views.createItemSelect
import support.views.toText

class EvaluationTargetForm() : BindingFormLayout<EvaluationTargetData>(EvaluationTargetData::class) {
    private val evaluationItemScores: MutableList<EvaluationItemScoreForm> = mutableListOf()
    private val note: TextArea = TextArea("기타 특이사항")
    private val evaluationStatus: Select<EvaluationStatus> = createItemSelect<EvaluationStatus>("평가 상태").apply {
        setItems(*EvaluationStatus.values())
        setItemLabelGenerator { it.toText() }
    }
    private val sumOfScore: IntegerField = IntegerField("합계").apply { isReadOnly = true }

    init {
        add(H3("평가하기"), note, evaluationStatus, sumOfScore)
        setColspan(note, 2)
        drawRequired()
    }

    constructor(evaluationItems: List<EvaluationItemResponse>, evaluationItemId: Long?) : this() {
        evaluationItems.forEach {
            val answerForm = EvaluationItemScoreForm(it.title, it.description, it.maximumScore).apply {
                setColspan(this, 2)
                if (it.id == evaluationItemId) {
                    changeTextColor("red")
                }
            }
            evaluationItemScores.add(answerForm)
            addComponentAtIndex(getIndexOfLastAnswer(), answerForm)
        }
    }

    private fun sumOfScore(): Int = evaluationItemScores.sumOf { it.score.value }

    private fun getIndexOfLastAnswer(): Int = (children.count() - FIXED_ADDED_COMPONENT_COUNT).toInt()

    override fun bindOrNull(): EvaluationTargetData? {
        val result = bindDefaultOrNull()
        val answers = evaluationItemScores.mapNotNull { it.bindOrNull() }
        if (evaluationItemScores.size != answers.size) {
            return null
        }
        return result?.apply { evaluationItemScores = answers }
    }

    override fun fill(data: EvaluationTargetData) {
        fillDefault(data)
        (evaluationItemScores zip data.evaluationItemScores).forEach {
            it.first.apply {
                fill(it.second)
                setScoreChangeEvent { sumOfScore.value = sumOfScore() }
            }
        }
        sumOfScore.value = sumOfScore()
    }

    companion object {
        private const val FIXED_ADDED_COMPONENT_COUNT: Int = 3
    }
}
