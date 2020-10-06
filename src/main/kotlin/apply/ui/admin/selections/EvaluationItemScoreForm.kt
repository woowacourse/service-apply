package apply.ui.admin.selections

import apply.application.EvaluationItemScoreData
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.component.textfield.TextField
import dev.mett.vaadin.tooltip.Tooltips
import dev.mett.vaadin.tooltip.config.TooltipConfiguration
import support.views.BindingIdentityFormLayout
import support.views.createIntSelect

class EvaluationItemScoreForm() : BindingIdentityFormLayout<EvaluationItemScoreData>(EvaluationItemScoreData::class) {
    val score: Select<Int> = createIntSelect(max = 0)
    private val title: TextField = TextField().apply { isReadOnly = true }

    init {
        add(title, score)
        drawRequired()
    }

    constructor(title: String, description: String, maximumScore: Int) : this() {
        this.score.setItems((0..maximumScore).toList())
        this.title.value = title
        val descriptionTooltip = TooltipConfiguration(description).apply { interactive = true }
        Tooltips.getCurrent().setTooltip(this.title, descriptionTooltip)
    }

    fun setScoreChangeEvent(scoreChangeEvent: () -> Unit) {
        score.addValueChangeListener { scoreChangeEvent() }
    }

    override fun bindOrNull(): EvaluationItemScoreData? {
        return bindDefaultOrNull()
    }

    override fun fill(data: EvaluationItemScoreData) {
        fillDefault(data)
    }
}
