package apply.ui.admin

import apply.ui.admin.cheater.CheatersView
import apply.ui.admin.evaluation.EvaluationsView
import apply.ui.admin.mail.MailView
import apply.ui.admin.mission.MissionSelectionsView
import apply.ui.admin.recruitment.RecruitmentsView
import apply.ui.admin.selections.SelectionsView
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.applayout.AppLayout
import com.vaadin.flow.component.applayout.DrawerToggle
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.html.Image
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.tabs.Tab
import com.vaadin.flow.component.tabs.Tabs
import com.vaadin.flow.component.tabs.TabsVariant
import com.vaadin.flow.router.RouterLink
import com.vaadin.flow.theme.Theme
import com.vaadin.flow.theme.lumo.Lumo

@Theme(value = Lumo::class)
class BaseLayout : AppLayout() {
    private val route: Map<String, Class<out Component>> = mapOf(
        "모집 관리" to RecruitmentsView::class.java,
        "평가 관리" to EvaluationsView::class.java,
        "과제 관리" to MissionSelectionsView::class.java,
        "선발 과정" to SelectionsView::class.java,
        "부정 행위자" to CheatersView::class.java,
        "메일 관리" to MailView::class.java
    )

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
            add(createTitle(), createLogo(width), createMenu())
        }
    }

    private fun createTitle(): Component {
        return HorizontalLayout(H1("지원 플랫폼"))
    }

    private fun createLogo(width: String): Component {
        return Image("images/logo/logo_full_white.png", "우아한테크코스").apply {
            maxWidth = width
        }
    }

    private fun createMenu(): Component {
        return Tabs().apply {
            orientation = Tabs.Orientation.VERTICAL
            addThemeVariants(TabsVariant.LUMO_MINIMAL)
            add(*createMenuItems().toTypedArray())
        }
    }

    private fun createMenuItems(): List<Component> {
        return route.map { (title, navigationTarget) ->
            RouterLink(title, navigationTarget)
        }.map(::createTab)
    }

    private fun createTab(component: Component): Component {
        return Tab(component)
    }
}
