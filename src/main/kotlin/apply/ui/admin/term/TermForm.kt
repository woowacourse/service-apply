package apply.ui.admin.term

import apply.application.TermData
import com.vaadin.flow.component.textfield.TextField
import support.views.BindingIdentityFormLayout

class TermForm : BindingIdentityFormLayout<TermData>(TermData::class) {
    private val name: TextField = TextField("기수 이름")

    init {
        add(name)
        setResponsiveSteps(ResponsiveStep("0", 1))
        drawRequired()
    }

    override fun bindOrNull(): TermData? {
        return bindDefaultOrNull()
    }

    override fun fill(term: TermData) {
        fillDefault(term)
    }
}
