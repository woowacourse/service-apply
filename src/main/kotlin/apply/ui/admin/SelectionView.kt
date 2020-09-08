package apply.ui.admin

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.BeforeEvent
import com.vaadin.flow.router.HasUrlParameter
import com.vaadin.flow.router.Route

@Route(value = "admin/selections", layout = BaseLayout::class)
class SelectionView(private var recruitmentId: Long) : VerticalLayout(), HasUrlParameter<Long> {
    init {
        add(createTitle(), createButton())
    }

    private fun createTitle(): Component {
        return HorizontalLayout(H1("웹 백엔드 3기")).apply {
            setSizeFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        }
    }

    private fun createButton(): Component {
        return HorizontalLayout(Button("다운로드")).apply {
            setSizeFull()
            justifyContentMode = FlexComponent.JustifyContentMode.END
        }
    }

    override fun setParameter(event: BeforeEvent?, parameter: Long) {
        this.recruitmentId = parameter
    }
}
