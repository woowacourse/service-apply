package apply.ui.admin.recruitment

import apply.application.RecruitmentRequest
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.datetimepicker.DateTimePicker
import com.vaadin.flow.component.textfield.TextField
import support.createErrorSmallButton
import support.createPrimarySmallButton
import support.views.BindingFormLayout
import java.time.LocalDateTime

class RecruitmentForm() : BindingFormLayout<RecruitmentRequest>(RecruitmentRequest::class) {
    private val title: TextField = TextField("모집명")
    private val startDateTime: DateTimePicker = DateTimePicker("시작 일시")
    private val endDateTime: DateTimePicker = DateTimePicker("종료 일시")
    private val recruitmentItems: MutableList<RecruitmentItemForm> = mutableListOf()

    init {
        add(title, startDateTime, endDateTime)
        addFormItem(createAddButton(), "모집 항목")
        setColspan(title, 2)
        drawRequired()
    }

    constructor(title: String, startDateTime: LocalDateTime, endDateTime: LocalDateTime) : this() {
        this.title.value = title
        this.startDateTime.value = startDateTime
        this.endDateTime.value = endDateTime
    }

    private fun createAddButton(): Button {
        return createPrimarySmallButton("추가하기") {
            val deleteButton = createErrorSmallButton("삭제") {}
            val item = RecruitmentItemForm().also { recruitmentItems.add(it) }
            val formItem = addFormItem(item, deleteButton).also { setColspan(it, 2) }
            deleteButton.addClickListener {
                it.unregisterListener()
                recruitmentItems.remove(item)
                remove(formItem)
            }
        }
    }

    override fun bindOrNull(): RecruitmentRequest? {
        val result = bindDefaultOrNull()
        val items = recruitmentItems.mapNotNull { it.bindOrNull() }
        if (recruitmentItems.size != items.size) {
            return null
        }
        return result?.apply { recruitmentItems = items }
    }
}
