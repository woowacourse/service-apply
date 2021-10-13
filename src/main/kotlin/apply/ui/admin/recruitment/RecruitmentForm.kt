package apply.ui.admin.recruitment

import apply.application.RecruitmentData
import apply.application.RecruitmentItemData
import apply.application.TermData
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.datetimepicker.DateTimePicker
import com.vaadin.flow.component.radiobutton.RadioButtonGroup
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.component.textfield.TextField
import support.views.BindingIdentityFormLayout
import support.views.createBooleanRadioButtonGroup
import support.views.createErrorSmallButton
import support.views.createItemSelect
import support.views.createPrimarySmallButton
import java.time.LocalDateTime

class RecruitmentForm() : BindingIdentityFormLayout<RecruitmentData>(RecruitmentData::class) {
    private val title: TextField = TextField("모집명")
    private val term: Select<TermData> = createItemSelect<TermData>("기수").apply {
        setItemLabelGenerator(TermData::name)
        isEmptySelectionAllowed = false
    }
    private val startDateTime: DateTimePicker = DateTimePicker("시작 일시")
    private val endDateTime: DateTimePicker = DateTimePicker("종료 일시")
    private val recruitable: RadioButtonGroup<Boolean> = createBooleanRadioButtonGroup("모집 여부", "모집 시작", "모집 중지")
    private val hidden: RadioButtonGroup<Boolean> = createBooleanRadioButtonGroup("공개 여부", "비공개", "공개", true)
    private val recruitmentItems: MutableList<RecruitmentItemForm> = mutableListOf()

    init {
        add(title, term, startDateTime, endDateTime, recruitable, hidden)
        addFormItem(createAddButton(), "모집 항목")
        setResponsiveSteps(ResponsiveStep("0", 1))
        drawRequired()
    }

    constructor(terms: List<TermData>) : this() {
        term.setItems(terms)
    }

    constructor(
        title: String,
        term: TermData,
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime,
        recruitable: Boolean,
        hidden: Boolean
    ) : this() {
        this.title.value = title
        this.term.value = term
        this.startDateTime.value = startDateTime
        this.endDateTime.value = endDateTime
        this.recruitable.value = recruitable
        this.hidden.value = hidden
    }

    private fun createAddButton(): Button {
        return createPrimarySmallButton("추가하기") {
            addRecruitmentItemForm()
        }
    }

    private fun addRecruitmentItemForm(data: RecruitmentItemData = RecruitmentItemData()) {
        val deleteButton = createErrorSmallButton("삭제") {}
        val item = RecruitmentItemForm().also {
            it.fill(data)
            recruitmentItems.add(it)
        }
        val formItem = addFormItem(item, deleteButton).also { setColspan(it, 2) }
        deleteButton.addClickListener {
            it.unregisterListener()
            recruitmentItems.remove(item)
            remove(formItem)
        }
    }

    override fun bindOrNull(): RecruitmentData? {
        val result = bindDefaultOrNull()
        val items = recruitmentItems.mapNotNull { it.bindOrNull() }
        if (recruitmentItems.size != items.size) {
            return null
        }
        return result?.apply { recruitmentItems = items }
    }

    override fun fill(data: RecruitmentData) {
        fillDefault(data)
        term.isReadOnly = true
        data.recruitmentItems.forEach { addRecruitmentItemForm(it) }
    }
}
