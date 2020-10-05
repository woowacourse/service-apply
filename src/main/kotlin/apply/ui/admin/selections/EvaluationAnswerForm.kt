package apply.ui.admin.selections

import apply.application.EvaluationAnswerData
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.component.textfield.TextField
import support.views.BindingIdentityFormLayout

class EvaluationAnswerForm() : BindingIdentityFormLayout<EvaluationAnswerData>(EvaluationAnswerData::class) {
    val score: Select<Int> = Select(0)
    private val title: TextField = TextField().apply {
        isReadOnly = true
        element.style.set("fontWeight", "bold")
    }
    private val description: TextArea = TextArea().apply { isReadOnly = true }

    init {
        add(title, score, description)
        setColspan(description, 2)
        drawRequired()
    }

    constructor(title: String, description: String, maximumScore: Int) : this() {
        this.score.setItems((0..maximumScore).toList())
        this.title.value = title
        this.description.value = description
    }

    fun setScoreChangeEvent(scoreChangeEvent: () -> Unit) {
        score.addValueChangeListener { scoreChangeEvent() }
    }

    override fun bindOrNull(): EvaluationAnswerData? {
        return bindDefaultOrNull()
    }

    override fun fill(data: EvaluationAnswerData) {
        fillDefault(data)
    }
}
