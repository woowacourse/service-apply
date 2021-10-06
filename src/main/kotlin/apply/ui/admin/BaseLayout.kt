package apply.ui.admin

import apply.application.RecruitmentResponse
import apply.application.RecruitmentService
import apply.ui.admin.cheater.CheatersView
import apply.ui.admin.evaluation.EvaluationsView
import apply.ui.admin.mail.MailFormView
import apply.ui.admin.recruitment.RecruitmentsView
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.accordion.Accordion
import com.vaadin.flow.component.applayout.AppLayout
import com.vaadin.flow.component.applayout.DrawerToggle
import com.vaadin.flow.component.details.DetailsVariant
import com.vaadin.flow.component.html.Anchor
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
class BaseLayout(
    private val recruitmentService: RecruitmentService
) : AppLayout() {
    private val route: Map<String, Any> = mapOf(
        "모집 관리" to RecruitmentsView::class.java,
        "평가 관리" to EvaluationsView::class.java,
        "과제 관리" to "/admin/missions",
        "선발 과정" to "/admin/selections",
        "부정 행위자" to CheatersView::class.java,
        "메일 발송" to MailFormView::class.java
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
        val recruitments = recruitmentService.findAll()
        val menuItems = route.map {
            when (val target = it.value) {
                is String -> createAccordion(it.key, recruitments.toTabs(target))
                is Class<*> -> createRouterLinkTab(it.key, target)
                else -> throw IllegalArgumentException()
            }
        }
        return createTabs(menuItems)
    }

    private fun List<RecruitmentResponse>.toTabs(path: String): Component {
        return createTabs(map { Tab(Anchor("$path/${it.id}", it.title)) })
    }

    private fun createAccordion(summary: String, component: Component): Component {
        return Accordion().apply {
            add(summary, component).addThemeVariants(DetailsVariant.REVERSE)
            close()
        }
    }

    private fun createRouterLinkTab(text: String, navigationTarget: Class<*>): Component {
        @Suppress("UNCHECKED_CAST")
        return Tab(RouterLink(text, navigationTarget as Class<out Component>))
    }

    private fun createTabs(components: List<Component>): Component {
        return Tabs().apply {
            orientation = Tabs.Orientation.VERTICAL
            isAutoselect = false
            addThemeVariants(TabsVariant.LUMO_MINIMAL)
            add(*components.toTypedArray())
        }
    }
}
