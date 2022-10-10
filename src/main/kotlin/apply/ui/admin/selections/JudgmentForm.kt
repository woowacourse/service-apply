package apply.ui.admin.selections

import apply.application.JudgmentData
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.textfield.IntegerField
import com.vaadin.flow.component.textfield.TextArea
import support.views.BindingFormLayout

class JudgmentForm() : BindingFormLayout<JudgmentData>(JudgmentData::class) {

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

    constructor(judgmentData: JudgmentData) : this() {
        judgmentData.judgmentResult
        judgmentData.passCount
        judgmentData.totalCount
    }

    override fun fill(data: JudgmentData) {
        fillDefault(data)
        judgmentResult.isReadOnly = true
        passCount.isReadOnly = true
        totalCount.isReadOnly = true
    }

    override fun bindOrNull(): JudgmentData? {
        return bindDefaultOrNull()
    }
}
