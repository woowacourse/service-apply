package apply.ui.admin

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
        "모집 관리" to RecruitmentView::class.java,
        "평가 관리" to EvaluationView::class.java,
        "선발 과정" to SelectionsView::class.java,
        "부정 행위자" to CheaterView::class.java
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
