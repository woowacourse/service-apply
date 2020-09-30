package apply.ui.admin.selections

import apply.application.EvaluationAnswerRequest
import apply.application.GradeEvaluationItemResponse
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.component.textfield.TextArea
import support.views.BindingFormLayout
import support.views.LongField

class EvaluationAnswerForm() : BindingFormLayout<EvaluationAnswerRequest>(EvaluationAnswerRequest::class) {
    val score: Select<Int> = Select(0)
    private val title: Button = Button().apply { element.style.set("fontWeight", "bold") }
    private val description: TextArea = TextArea().apply {
        isReadOnly = true
        isVisible = false
    }
    private var evaluationItemId: LongField = LongField()

    init {
        add(title, score, description)
        setColspan(description, 2)
        drawRequired()
    }

    constructor(evaluationItem: GradeEvaluationItemResponse, valueChangeBehavior: () -> Unit) : this() {
        score.apply {
            addValueChangeListener { valueChangeBehavior() }
            setItems((0..evaluationItem.maximumScore).toList())
            value = evaluationItem.score
        }
        title.apply {
            addClickListener { description.isVisible = !description.isVisible }
            text = evaluationItem.title
        }
        description.value = evaluationItem.description
        evaluationItemId.value = evaluationItem.id
    }

    override fun bindOrNull(): EvaluationAnswerRequest? {
        return bindDefaultOrNull()
    }
}
