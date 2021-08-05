package apply.ui.admin.selections

import apply.application.RecruitmentResponse
import apply.application.RecruitmentService
import apply.ui.admin.BaseLayout
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.Route
import support.views.createNormalButton

@Route(value = "admin/selections", layout = BaseLayout::class)
class SelectionsView(private val recruitmentService: RecruitmentService) : VerticalLayout() {
    init {
        add(createTitle(), *createButtons())
    }

    private fun createTitle(): Component {
        return HorizontalLayout(H1("선발 과정")).apply {
            setSizeFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        }
    }

    private fun createButtons(): Array<Component> {
        return recruitmentService.findAll()
            .map {
                createButton(it)
            }
            .toTypedArray()
    }

    private fun createButton(recruitment: RecruitmentResponse): Component {
        return HorizontalLayout(
            createNormalButton(recruitment.title) {
                UI.getCurrent().navigate(SelectionView::class.java, recruitment.id)
            }
        ).apply {
            setSizeFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        }
    }
}
