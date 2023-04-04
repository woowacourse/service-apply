package apply.ui.admin

import apply.application.RecruitmentResponse
import apply.application.RecruitmentService
import apply.ui.admin.administrator.AdministratorsView
import apply.ui.admin.cheater.CheatersView
import apply.ui.admin.evaluation.EvaluationsView
import apply.ui.admin.mail.MailsView
import apply.ui.admin.mission.MissionsView
import apply.ui.admin.recruitment.RecruitmentsView
import apply.ui.admin.selections.SelectionView
import apply.ui.admin.term.TermsView
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.applayout.AppLayout
import com.vaadin.flow.component.applayout.DrawerToggle
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.html.Hr
import com.vaadin.flow.component.html.Image
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.tabs.Tabs
import com.vaadin.flow.theme.Theme
import com.vaadin.flow.theme.lumo.Lumo
import support.views.createItemSelect
import support.views.createTabs

@Theme(value = Lumo::class)
class BaseLayout(
    private val recruitmentService: RecruitmentService
) : AppLayout() {
    private val recruitmentsFilter: VerticalLayout = createRecruitmentsFilter()
    private val menuTabs: List<Tabs> = emptyList()

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
            add(
                createTitle(),
                createLogo(width),
                createRecruitmentSelect(),
                recruitmentsFilter,
                Hr(),
                createMenu(createMenuItems()),
                Hr(),
                createMenu(createSettingMenuItems())
            )
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

    private fun createMenu(menuItems: List<MenuItem>): Tabs {
        return createTabs(menuItems.map { it.toComponent() }).apply {
            addSelectedChangeListener { event ->
                menuTabs.filter { it != event.source }.forEach { it.selectedTab = null }
            }
        }
    }

    private fun createRecruitmentsFilter(): VerticalLayout {
        val recruitmentMenuComponents = createMenu(createRecruitmentsMenuItems(null))
        return VerticalLayout(recruitmentMenuComponents).apply {
            isPadding = false
            isMargin = false
            minHeight = "200px"
            alignItems = FlexComponent.Alignment.CENTER
        }
    }

    private fun createRecruitmentSelect(): Component {
        return createItemSelect<RecruitmentResponse>("모집을 선택해 선발을 진행하세요.").apply {
            setItems(recruitmentService.findAll())
            setItemLabelGenerator { it.title }
            placeholder = "모집 선택"
            addValueChangeListener { event ->
                val recruitmentMenuComponents = createMenu(createRecruitmentsMenuItems(event.value.id))
                recruitmentsFilter.removeAll()
                recruitmentsFilter.add(recruitmentMenuComponents)
            }
        }
    }

    private fun createRecruitmentsMenuItems(recruitmentId: Long?): List<SelectedSingleMenuItem> {
        if (recruitmentId == null) {
            return emptyList()
        }

        return listOf(
            "과제 관리".selectedOf(MissionsView::class.java, recruitmentId),
            "선발 관리".selectedOf(SelectionView::class.java, recruitmentId)
        )
    }

    private fun createMenuItems(): List<MenuItem> {
        return listOf(
            "기수 관리" of TermsView::class.java,
            "모집 관리" of RecruitmentsView::class.java,
            "평가 관리" of EvaluationsView::class.java
        )
    }

    private fun createSettingMenuItems(): List<MenuItem> {
        return listOf(
            "부정행위자" of CheatersView::class.java,
            "메일 관리" of MailsView::class.java,
            "관리자" of AdministratorsView::class.java
        )
    }
}
