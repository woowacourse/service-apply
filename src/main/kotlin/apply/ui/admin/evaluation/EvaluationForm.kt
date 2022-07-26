package apply.ui.admin.evaluation

import apply.application.EvaluationData
import apply.application.EvaluationItemData
import apply.application.EvaluationSelectData
import apply.application.RecruitmentSelectData
import apply.domain.evaluation.Evaluation
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.component.textfield.TextField
import support.views.BindingIdentityFormLayout
import support.views.createErrorSmallButton
import support.views.createItemSelect
import support.views.createPrimarySmallButton

class EvaluationForm() : BindingIdentityFormLayout<EvaluationData>(EvaluationData::class) {
    private val title: TextField = TextField("평가명")
    private val description: TextArea = TextArea("설명")
    private val recruitment: Select<RecruitmentSelectData> = createItemSelect<RecruitmentSelectData>("모집").apply {
        setItemLabelGenerator(RecruitmentSelectData::title)
        isEmptySelectionAllowed = false
    }
    private val beforeEvaluation: Select<EvaluationSelectData> = createItemSelect<EvaluationSelectData>("이전 평가").apply {
        setItemLabelGenerator(EvaluationSelectData::title)
    }
    private val evaluationItems: MutableList<EvaluationItemForm> = mutableListOf()

    init {
        add(title, recruitment, beforeEvaluation, description)
        setResponsiveSteps(ResponsiveStep("0", 1))
        addFormItem(createAddButton(), "평가 항목")
        drawRequired()
    }

    constructor(
        recruitments: List<RecruitmentSelectData>,
        listener: (id: Long) -> List<EvaluationSelectData>
    ) : this() {
        recruitment.setItems(recruitments)
        recruitment.addValueChangeListener {
            val evaluations: List<EvaluationSelectData> = mutableListOf(
                EvaluationSelectData(
                    Evaluation(
                        title = "이전 평가 없음",
                        description = "이전 평가 없음",
                        recruitmentId = it.value.id
                    )
                )
            )
            beforeEvaluation.setItems(evaluations.plus(listener(it.value.id)))
        }
    }

    private fun createAddButton(): Button {
        return createPrimarySmallButton("추가하기") {
            val deleteButton = createErrorSmallButton("삭제") {}
            val item = EvaluationItemForm().also { evaluationItems.add(it) }
            val formItem = addFormItem(item, deleteButton).also { setColspan(it, 2) }
            deleteButton.addClickListener {
                it.unregisterListener()
                evaluationItems.remove(item)
                remove(formItem)
            }
        }
    }

    private fun addEvaluationItemForm(data: EvaluationItemData = EvaluationItemData()) {
        val deleteButton = createErrorSmallButton("삭제") {}
        val item = EvaluationItemForm().also {
            it.fill(data)
            evaluationItems.add(it)
        }
        val formItem = addFormItem(item, deleteButton).also { setColspan(it, 2) }
        deleteButton.addClickListener {
            it.unregisterListener()
            evaluationItems.remove(item)
            remove(formItem)
        }
    }

    override fun bindOrNull(): EvaluationData? {
        val result = bindDefaultOrNull()
        val items = evaluationItems.mapNotNull { it.bindOrNull() }
        if (evaluationItems.size != items.size) {
            return null
        }
        if (!beforeEvaluation.isEmpty) {
            result?.beforeEvaluation = beforeEvaluation.value
        }
        return result?.apply {
            evaluationItems = items
        }
    }

    override fun fill(data: EvaluationData) {
        fillDefault(data)
        data.evaluationItems.forEach { addEvaluationItemForm(it) }
    }
}
