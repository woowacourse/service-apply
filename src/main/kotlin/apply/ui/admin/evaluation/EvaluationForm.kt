package apply.ui.admin.evaluation

import apply.application.EvaluationRequest
import apply.domain.evaluation.Evaluation
import apply.domain.recruitment.Recruitment
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.component.textfield.TextField
import support.views.BindingFormLayout
import support.views.createErrorSmallButton
import support.views.createItemSelect
import support.views.createPrimarySmallButton

class EvaluationForm() : BindingFormLayout<EvaluationRequest>(EvaluationRequest::class) {
    private val title: TextField = TextField("평가명")
    private val description: TextArea = TextArea("설명")
    private val recruitment = createItemSelect<Recruitment>("모집").apply {
        setItemLabelGenerator(Recruitment::title)
        isEmptySelectionAllowed = false
    }
    private val beforeEvaluation = createItemSelect<Evaluation>("이전 평가").apply {
        setItemLabelGenerator(Evaluation::title)
    }
    private val evaluationItems: MutableList<EvaluationItemForm> = mutableListOf()

    constructor(recruitments: List<Recruitment>, listener: (id: Long) -> List<Evaluation>) : this() {
        recruitment.setItems(recruitments)
        recruitment.addValueChangeListener {
            beforeEvaluation.setItems(listener.invoke(it.value.id))
        }
        add(title, recruitment, beforeEvaluation, description)
        setColspan(title, 2)
        setColspan(recruitment, 2)
        setColspan(beforeEvaluation, 2)
        setColspan(description, 2)
        addFormItem(createAddButton(), "평가 항목")
        drawRequired()
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

    override fun bindOrNull(): EvaluationRequest? {
        val result = bindDefaultOrNull()
        val items = evaluationItems.mapNotNull { it.bindOrNull() }
        if (evaluationItems.size != items.size) {
            return null
        }
        if (!beforeEvaluation.isEmpty) {
            result?.beforeEvaluationId = beforeEvaluation.value.id
        }
        return result?.apply {
            recruitmentId = recruitment.value.id
            evaluationItems = items
        }
    }
}
