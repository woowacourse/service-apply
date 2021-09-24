package apply.ui.admin.mission

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

@Route(value = "admin/missions", layout = BaseLayout::class)
class MissionSelectionsView(private val recruitmentService: RecruitmentService) : VerticalLayout() {
    private val title: Component = createTitle()
    private val buttons: Array<Component> = createButtons()
    init {
        add(title, *buttons)
    }

    private fun createTitle(): Component {
        return HorizontalLayout(H1("모집 선택")).apply {
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
                UI.getCurrent().navigate(MissionSelectionView::class.java, recruitment.id)
            }
        ).apply {
            setSizeFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        }
    }
}
