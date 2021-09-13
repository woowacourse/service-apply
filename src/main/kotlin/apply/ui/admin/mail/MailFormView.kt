package apply.ui.admin.mail

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import support.views.Title

open abstract class MailFormView(title: String) : VerticalLayout() {
    init {
        add(Title(title), createMailForm())
    }

    private fun createMailForm(): Component {

        return Div()
    }

    open abstract fun createReceiverFilter()
}
