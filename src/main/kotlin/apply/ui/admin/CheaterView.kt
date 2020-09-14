package apply.ui.admin

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.Route

@Route(value = "admin/cheater", layout = BaseLayout::class)
class CheaterView : VerticalLayout() {
    init {
        add(createTitle())
    }

    private fun createTitle(): Component {
        return HorizontalLayout(H1("부정 행위자")).apply {
            setSizeFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        }
    }
}
