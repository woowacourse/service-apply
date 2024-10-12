package apply.ui.admin

import apply.application.RecruitmentService
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.applayout.AppLayout
import com.vaadin.flow.component.applayout.DrawerToggle
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.theme.Theme
import com.vaadin.flow.theme.lumo.Lumo

@Theme(value = Lumo::class)
class BaseLayout(
    private val recruitmentService: RecruitmentService
) : AppLayout() {
    init {
        primarySection = Section.DRAWER
        addToDrawer(createDrawer())
        addToNavbar(DrawerToggle())
    }

    private fun createDrawer(): Component {
        return VerticalLayout().apply {
            setSizeFull()
            themeList["dark"] = true
            alignItems = FlexComponent.Alignment.CENTER
            add(createTitle(), MenuLayout(recruitmentService))
        }
    }

    private fun createTitle(): Component {
        return HorizontalLayout(H1("지원 플랫폼"))
    }
}
