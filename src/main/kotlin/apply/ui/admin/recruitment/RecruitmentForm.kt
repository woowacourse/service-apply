package apply.ui.admin.recruitment

import apply.application.RecruitmentData
import apply.application.RecruitmentItemData
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.datetimepicker.DateTimePicker
import com.vaadin.flow.component.radiobutton.RadioButtonGroup
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.binder.PropertyId
import support.views.BindingIdentityFormLayout
import support.views.createBooleanRadioButtonGroup
import support.views.createErrorSmallButton
import support.views.createPrimarySmallButton
import java.time.LocalDateTime

class RecruitmentForm() : BindingIdentityFormLayout<RecruitmentData>(RecruitmentData::class) {
    private val title: TextField = TextField("모집명")
    private val startDateTime: DateTimePicker = DateTimePicker("시작 일시")
    private val endDateTime: DateTimePicker = DateTimePicker("종료 일시")
    private val canRecruit: RadioButtonGroup<Boolean> = createBooleanRadioButtonGroup("모집 여부", "모집 시작", "모집 중지")

    @PropertyId("hidden")
    private val isHidden: RadioButtonGroup<Boolean> = createBooleanRadioButtonGroup("공개 여부", "비공개", "공개", true)
    private val recruitmentItems: MutableList<RecruitmentItemForm> = mutableListOf()

    init {
        add(title, startDateTime, endDateTime, canRecruit, isHidden)
        addFormItem(createAddButton(), "모집 항목")
        setResponsiveSteps(ResponsiveStep("0", 1))
        drawRequired()
    }

    constructor(
        title: String,
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime,
        canRecruit: Boolean,
        isHidden: Boolean
    ) : this() {
        this.title.value = title
        this.startDateTime.value = startDateTime
        this.endDateTime.value = endDateTime
        this.canRecruit.value = canRecruit
        this.isHidden.value = isHidden
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
        data.recruitmentItems.forEach { addRecruitmentItemForm(it) }
    }
}
