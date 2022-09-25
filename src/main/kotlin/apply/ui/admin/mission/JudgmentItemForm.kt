package apply.ui.admin.mission

import apply.application.EvaluationItemData
import apply.application.JudgmentItemData
import apply.domain.mission.ProgrammingLanguage
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.component.textfield.TextField
import support.views.BindingIdentityFormLayout
import support.views.createItemSelect

class JudgmentItemForm() : BindingIdentityFormLayout<JudgmentItemData>(JudgmentItemData::class) {
    private var judgmentItemId: Long = 0L
    private val testName: TextField = TextField("채점에 사용할 미션 이름")
    private val programmingLanguage: Select<ProgrammingLanguage> =
        createItemSelect<ProgrammingLanguage>("언어 선택").apply {
            setItems(*ProgrammingLanguage.values())
            setItemLabelGenerator { it.language }
            value = ProgrammingLanguage.NONE
        }
    private val evaluationItemData: Select<EvaluationItemData> =
        createItemSelect<EvaluationItemData>("평가 항목").apply {
            setItemLabelGenerator(EvaluationItemData::title)
            isEmptySelectionAllowed = false
        }

    init {
        add(testName, programmingLanguage, evaluationItemData)
        setResponsiveSteps(ResponsiveStep("0", 1))
        drawRequired()
    }

    fun setEvaluationItem(evaluationItemsData: List<EvaluationItemData>) {
        evaluationItemData.setItems(evaluationItemsData)
        evaluationItemData.value = EvaluationItemData()
    }

    override fun bindOrNull(): JudgmentItemData? {
        return bindDefaultOrNull().apply {
            this?.id = judgmentItemId
        }
    }

    override fun fill(data: JudgmentItemData) {
        judgmentItemId = data.id
        fillDefault(data)
    }
}
