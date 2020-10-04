package apply.ui.admin.selections

import apply.application.GradeEvaluationData
import apply.application.GradeEvaluationItemData
import apply.domain.evaluationtarget.EvaluationStatus
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.component.textfield.IntegerField
import com.vaadin.flow.component.textfield.TextArea
import support.views.BindingFormLayout

class GradeEvaluationForm :
    BindingFormLayout<GradeEvaluationData>(GradeEvaluationData::class) {

    private val gradeEvaluationItems: MutableList<GradeEvaluationItemForm> = mutableListOf()
    private val note: TextArea = TextArea("기타 특이사항")
    private val evaluationStatus: Select<EvaluationStatus> = Select(*EvaluationStatus.values()).apply {
        setItemLabelGenerator { it.label() }
        label = "평가 상태"
    }
    private val sumOfScore: IntegerField = IntegerField("합계").apply { isReadOnly = true }

    init {
        add(note, evaluationStatus, sumOfScore)
        setColspan(note, 2)
        drawRequired()
    }

    private fun addGradeEvaluationItem(data: GradeEvaluationItemData = GradeEvaluationItemData()) {
        val evaluationAnswerForm = GradeEvaluationItemForm().also {
            it.fill(data)
            it.setScoreChangeEvent { sumOfScore.value = sumOfScore() }
            setColspan(it, 2)
            gradeEvaluationItems.add(it)
        }
        addComponentAtIndex((children.count() - 3).toInt(), evaluationAnswerForm)
    }

    private fun EvaluationStatus.label() =
        when (this) {
            EvaluationStatus.WAITING -> "평가 전"
            EvaluationStatus.PASS -> "합격"
            EvaluationStatus.FAIL -> "탈락"
            EvaluationStatus.PENDING -> "보류"
        }

    private fun sumOfScore() = gradeEvaluationItems.map { it.score.value }.sum()

    override fun bindOrNull(): GradeEvaluationData? {
        val result = bindDefaultOrNull()
        val answers = gradeEvaluationItems.mapNotNull { it.bindOrNull() }
        if (gradeEvaluationItems.size != answers.size) {
            return null
        }
        return result?.apply { gradeEvaluationItems = answers }
    }

    override fun fill(data: GradeEvaluationData) {
        fillDefault(data)
        data.gradeEvaluationItems.forEach { addGradeEvaluationItem(it) }
        sumOfScore.value = sumOfScore()
    }
}
