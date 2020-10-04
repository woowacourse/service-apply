package apply.ui.admin.selections

import apply.application.GradeEvaluationItemData
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.component.textfield.TextField
import support.views.BindingIdentityFormLayout

class GradeEvaluationItemForm() : BindingIdentityFormLayout<GradeEvaluationItemData>(GradeEvaluationItemData::class) {
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

    constructor(score: Int, maximumScore: Int, title: String, description: String) : this() {
        this.score.apply {
            setItems((0..maximumScore).toList())
            value = score
        }
        this.title.value = title
        this.description.value = description
    }

    fun setScoreChangeEvent(scoreChangeEvent: () -> Unit) {
        score.addValueChangeListener { scoreChangeEvent() }
    }

    override fun bindOrNull(): GradeEvaluationItemData? {
        return bindDefaultOrNull()
    }

    override fun fill(data: GradeEvaluationItemData) {
        this.score.setItems((0..data.maximumScore).toList())
        fillDefault(data)
    }
}
