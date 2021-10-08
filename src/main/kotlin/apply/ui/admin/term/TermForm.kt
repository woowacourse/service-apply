package apply.ui.admin.term

import apply.application.TermSelectData
import com.vaadin.flow.component.textfield.TextField
import support.views.BindingIdentityFormLayout

class TermForm() : BindingIdentityFormLayout<TermSelectData>(TermSelectData::class) {
    val name: TextField = TextField("기수 이름")

    init {
        add(name)
        setResponsiveSteps(ResponsiveStep("0", 1))
        drawRequired()
    }

    override fun bindOrNull(): TermSelectData? {
        return bindDefaultOrNull()
    }

    override fun fill(term: TermSelectData) {
        name.placeholder = term.name
    }
}
