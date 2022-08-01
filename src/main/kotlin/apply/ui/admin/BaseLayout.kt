package apply.ui.admin

import apply.application.RecruitmentService
import apply.ui.admin.cheater.CheatersView
import apply.ui.admin.evaluation.EvaluationsView
import apply.ui.admin.mail.MailsView
import apply.ui.admin.recruitment.RecruitmentsView
import apply.ui.admin.term.TermsView
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.applayout.AppLayout
import com.vaadin.flow.component.applayout.DrawerToggle
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.html.Image
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.theme.Theme
import com.vaadin.flow.theme.lumo.Lumo
import support.views.createTabs

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
            element.style.set("overflow", "auto")
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
        return createTabs(createMenuItems().map { it.toComponent() })
    }

    private fun createMenuItems(): List<MenuItem> {
        val recruitments = recruitmentService.findAll()
        return listOf(
            "기수 관리" of TermsView::class.java,
            "모집 관리" of RecruitmentsView::class.java,
            "평가 관리" of EvaluationsView::class.java,
            "과제 관리".accordionOf("admin/missions", recruitments),
            "선발 과정".accordionOf("admin/selections", recruitments),
            "부정행위자" of CheatersView::class.java,
            "메일 관리" of MailsView::class.java
        )
    }
}
