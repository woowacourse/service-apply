package apply.ui.admin.mission

import apply.application.EvaluationItemSelectData
import apply.application.EvaluationSelectData
import apply.application.JudgmentItemData
import apply.application.MissionData
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.datetimepicker.DateTimePicker
import com.vaadin.flow.component.radiobutton.RadioButtonGroup
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.component.textfield.TextField
import support.views.BindingIdentityFormLayout
import support.views.createBooleanRadioButtonGroup
import support.views.createErrorSmallButton
import support.views.createItemSelect
import support.views.createPrimarySmallButton

class MissionForm() : BindingIdentityFormLayout<MissionData>(MissionData::class) {
    private val title: TextField = TextField("과제명")
    private val description: TextArea = TextArea("설명")
    private val evaluation: Select<EvaluationSelectData> = createItemSelect<EvaluationSelectData>("평가").apply {
        setItemLabelGenerator(EvaluationSelectData::title)
        isEmptySelectionAllowed = false
    }
    private val startDateTime: DateTimePicker = DateTimePicker("시작 일시")
    private val endDateTime: DateTimePicker = DateTimePicker("종료 일시")
    private val submittable: RadioButtonGroup<Boolean> = createBooleanRadioButtonGroup("제출 여부", "제출 시작", "제출 중지", false)
    private val hidden: RadioButtonGroup<Boolean> = createBooleanRadioButtonGroup("공개 여부", "비공개", "공개", true)
    private val addButton: Button = createAddButton()
    private val judgmentItemForm: JudgmentItemForm = JudgmentItemForm()

    init {
        add(title, evaluation, startDateTime, endDateTime, description, submittable, hidden)
        setResponsiveSteps(ResponsiveStep("0", 1))
        addFormItem(addButton, "자동 채점 항목")
        drawRequired()
    }

    constructor(
        evaluations: List<EvaluationSelectData>,
        listener: (id: Long) -> List<EvaluationItemSelectData>,
    ) : this() {
        evaluation.setItems(evaluations)
        evaluation.addValueChangeListener {
            judgmentItemForm.setEvaluationItem(listener(it.value.id))
        }
    }

    private fun createAddButton(): Button {
        return createPrimarySmallButton("추가하기") {
            addJudgmentItemForm()
        }
    }

    private fun addJudgmentItemForm() {
        val deleteButton = createErrorSmallButton("삭제") {}
        val formItem = addFormItem(judgmentItemForm, deleteButton).also { setColspan(it, 2) }
        addButton.isEnabled = false
        deleteButton.addClickListener {
            it.unregisterListener()
            addButton.isEnabled = true
            judgmentItemForm.fill(JudgmentItemData())
            remove(formItem)
        }
    }

    override fun bindOrNull(): MissionData? {
        val result = bindDefaultOrNull()
        val item = judgmentItemForm.bindOrNull() ?: return null
        return result?.apply { judgmentItemData = item }
    }

    override fun fill(data: MissionData) {
        fillDefault(data)
        evaluation.isReadOnly = true
        if (data.judgmentItemData != JudgmentItemData()) {
            addJudgmentItemForm()
            judgmentItemForm.fill(data.judgmentItemData)
        }
    }
}
