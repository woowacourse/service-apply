package apply.ui.admin.recruitment

import apply.application.RecruitmentItemData
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.component.textfield.IntegerField
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.component.textfield.TextField
import support.views.BindingIdentityFormLayout
import support.views.createIntSelect

class RecruitmentItemForm() : BindingIdentityFormLayout<RecruitmentItemData>(RecruitmentItemData::class) {
    private val title: TextField = TextField("항목명")
    private val position: Select<Int> = createIntSelect(max = 10).apply { label = "순서" }
    private val maximumLength: IntegerField = IntegerField("최대 글자 수")
    private val description: TextArea = TextArea("설명")

    init {
        add(title, position, maximumLength, description)
        setResponsiveSteps(ResponsiveStep("0", 1))
        drawRequired()
    }

    constructor(title: String, position: Int, maximumLength: Int, description: String) : this() {
        this.title.value = title
        this.position.value = position
        this.maximumLength.value = maximumLength
        this.description.value = description
    }

    override fun bindOrNull(): RecruitmentItemData? {
        return bindDefaultOrNull()
    }

    override fun fill(data: RecruitmentItemData) {
        fillDefault(data)
    }
}
