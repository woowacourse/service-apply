package apply.ui.admin.selections

import apply.application.EvaluationJudgementData
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.textfield.IntegerField
import com.vaadin.flow.component.textfield.TextArea
import support.views.BindingFormLayout

class EvaluationJudgmentForm() : BindingFormLayout<EvaluationJudgementData>(EvaluationJudgementData::class) {

    private val judgmentResult: TextArea = TextArea("채점 정보")
    private val passCount: IntegerField = IntegerField("맞춘 테스트케이스 개수")
    private val totalCount: IntegerField = IntegerField("총 테스트케이스 개수")

    init {
        judgmentResult.setMinHeight("135px")
        val formLayout = FormLayout()
        formLayout.add(passCount, totalCount)
        add(judgmentResult, formLayout)
        drawRequired()
    }

    constructor(evaluationJudgementData: EvaluationJudgementData) : this() {
        evaluationJudgementData.judgmentResult
        evaluationJudgementData.passCount
        evaluationJudgementData.totalCount
    }

    override fun fill(data: EvaluationJudgementData) {
        fillDefault(data)
        judgmentResult.isReadOnly = true
        passCount.isReadOnly = true
        totalCount.isReadOnly = true
    }

    override fun bindOrNull(): EvaluationJudgementData? {
        return bindDefaultOrNull()
    }
}
