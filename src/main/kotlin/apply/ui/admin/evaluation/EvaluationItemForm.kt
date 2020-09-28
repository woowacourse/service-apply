package apply.ui.admin.evaluation

import apply.application.EvaluationItemRequest
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.component.textfield.TextField
import support.views.BindingFormLayout
import support.views.createIntSelect

class EvaluationItemForm() : BindingFormLayout<EvaluationItemRequest>(EvaluationItemRequest::class) {
    private val title: TextField = TextField("항목명")
    private val maximumScore: Select<Int> = createIntSelect(max = 10).apply { label = "최대 점수" }
    private val position: Select<Int> = createIntSelect(max = 10).apply { label = "순서" }
    private val description: TextArea = TextArea("설명")

    init {
        add(title, maximumScore, position, description)
        setResponsiveSteps(ResponsiveStep("0", 1))
        drawRequired()
    }

    constructor(title: String, maximumScore: Int, position: Int, description: String) : this() {
        this.title.value = title
        this.maximumScore.value = maximumScore
        this.position.value = position
        this.description.value = description
    }

    override fun bindOrNull(): EvaluationItemRequest? {
        return bindDefaultOrNull()
    }
}
