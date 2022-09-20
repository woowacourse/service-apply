package apply.ui.admin.mission

import apply.application.EvaluationItemData
import apply.application.EvaluationSelectData
import apply.application.MissionData
import apply.domain.mission.ProgrammingLanguage
import com.vaadin.flow.component.datetimepicker.DateTimePicker
import com.vaadin.flow.component.html.Hr
import com.vaadin.flow.component.radiobutton.RadioButtonGroup
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.component.textfield.TextField
import support.views.BindingIdentityFormLayout
import support.views.createBooleanRadioButtonGroup
import support.views.createItemSelect

class MissionForm() : BindingIdentityFormLayout<MissionData>(MissionData::class) {
    private val title: TextField = TextField("과제명")
    private val description: TextArea = TextArea("설명")
    private val evaluation: Select<EvaluationSelectData> = createItemSelect<EvaluationSelectData>("평가").apply {
        setItemLabelGenerator(EvaluationSelectData::title)
        isEmptySelectionAllowed = false
    }
    private val startDateTime: DateTimePicker = DateTimePicker("시작 일시")
    private val endDateTime: DateTimePicker = DateTimePicker("종료 일시")

    private var judgmentItemId: Long = 0L
    private val testName: TextField = TextField("채점에 사용할 미션 이름")
    private val programmingLanguage: Select<ProgrammingLanguage> =
        createItemSelect<ProgrammingLanguage>("언어 선택").apply {
            setItems(*ProgrammingLanguage.values())
            setItemLabelGenerator { it.language }
            value = ProgrammingLanguage.NONE
        }
    private val evaluationItem: Select<EvaluationItemData> =
        createItemSelect<EvaluationItemData>("평가 항목").apply {
            setItemLabelGenerator(EvaluationItemData::title)
            isEmptySelectionAllowed = false
        }

    private val submittable: RadioButtonGroup<Boolean> = createBooleanRadioButtonGroup("제출 여부", "제출 시작", "제출 중지", false)
    private val hidden: RadioButtonGroup<Boolean> = createBooleanRadioButtonGroup("공개 여부", "비공개", "공개", true)

    init {
        add(
            title, evaluation, startDateTime, endDateTime, description,
            Hr(), testName, programmingLanguage, evaluationItem, Hr(),
            submittable, hidden
        )
        setResponsiveSteps(ResponsiveStep("0", 1))
        drawRequired()
    }

    constructor(
        evaluations: List<EvaluationSelectData>,
        listener: (id: Long) -> List<EvaluationItemData>,
    ) : this() {
        evaluation.setItems(evaluations)
        evaluation.addValueChangeListener {
            val evaluationItems: List<EvaluationItemData> = mutableListOf()
            evaluationItem.setItems(evaluationItems + listener(it.value.id))
            evaluationItem.value = EvaluationItemData()
        }
    }

    override fun bindOrNull(): MissionData? {
        return bindDefaultOrNull().apply {
            this?.judgmentItemId = judgmentItemId
        }
    }

    override fun fill(data: MissionData) {
        judgmentItemId = data.judgmentItemId
        fillDefault(data)
        evaluation.isReadOnly = true
    }
}
