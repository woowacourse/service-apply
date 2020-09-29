package apply.ui.admin.selections

import apply.application.DoEvaluationItemResponse
import apply.application.EvaluationAnswerRequest
import com.vaadin.flow.component.ClickEvent
import com.vaadin.flow.component.ComponentUtil
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.component.textfield.TextArea
import support.views.BindingFormLayout

class EvaluateAnswerForm() : BindingFormLayout<EvaluationAnswerRequest>(EvaluationAnswerRequest::class) {
    val score: Select<Int> = Select(0)
    private val title: Button = Button().apply { element.style.set("fontWeight", "bold") }
    private val description: TextArea = TextArea().apply {
        isReadOnly = true
        isVisible = false
    }
    private var evaluationItemId = 0L

    init {
        add(title, score, description)
        setColspan(description, 2)
        drawRequired()
    }

    constructor(evaluationItem: DoEvaluationItemResponse, reCalculateTotalScore: () -> Unit) : this() {
        this.title.apply {
            ComponentUtil.addListener(this, ClickEvent::class.java) {
                description.isVisible = !description.isVisible
            }
            text = evaluationItem.title
        }
        this.score.apply {
            addValueChangeListener { reCalculateTotalScore() }
            setItems((0..evaluationItem.maximumScore).toList())
            value = 0
        }
        this.description.value = evaluationItem.description
        this.evaluationItemId = evaluationItem.id
    }

    override fun bindOrNull(): EvaluationAnswerRequest? {
        return bindDefaultOrNull()
    }
}
