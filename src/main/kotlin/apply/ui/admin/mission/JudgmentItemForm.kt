package apply.ui.admin.mission

import apply.application.EvaluationItemSelectData
import apply.application.JudgmentItemData
import apply.domain.mission.ProgrammingLanguage
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.component.textfield.TextField
import support.views.BindingIdentityFormLayout
import support.views.createItemSelect

class JudgmentItemForm : BindingIdentityFormLayout<JudgmentItemData>(JudgmentItemData::class) {
    private val programmingLanguage: Select<ProgrammingLanguage> =
        createItemSelect<ProgrammingLanguage>("언어 선택").apply {
            setItems(*ProgrammingLanguage.values())
            setItemLabelGenerator { it.value }
            value = ProgrammingLanguage.NONE
        }
    private val testName: TextField = TextField("테스트 이름")
    private val evaluationItemSelectData: Select<EvaluationItemSelectData> =
        createItemSelect<EvaluationItemSelectData>("평가 항목").apply {
            setItemLabelGenerator(EvaluationItemSelectData::title)
            isEmptySelectionAllowed = false
        }

    init {
        add(programmingLanguage, testName, evaluationItemSelectData)
        setResponsiveSteps(ResponsiveStep("0", 1))
        drawRequired()
    }

    fun setEvaluationItem(items: List<EvaluationItemSelectData>) {
        evaluationItemSelectData.setItems(items)
        evaluationItemSelectData.value = EvaluationItemSelectData()
    }

    override fun bindOrNull(): JudgmentItemData? {
        return bindDefaultOrNull()
    }

    override fun fill(data: JudgmentItemData) {
        fillDefault(data)
    }
}
