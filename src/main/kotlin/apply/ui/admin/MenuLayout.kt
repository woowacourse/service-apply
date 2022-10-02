package apply.ui.admin

import apply.application.RecruitmentService
import apply.ui.admin.cheater.CheatersView
import apply.ui.admin.evaluation.EvaluationsView
import apply.ui.admin.mail.MailsView
import apply.ui.admin.mission.MissionsView
import apply.ui.admin.recruitment.RecruitmentsView
import apply.ui.admin.selections.SelectionView
import apply.ui.admin.term.TermsView
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.tabs.Tabs
import support.views.createTabs

private const val UNSELECT_ALL: Int = -1

class MenuLayout(
    private val recruitmentService: RecruitmentService
) : VerticalLayout() {
    private val topMenu = createMenu(createTopMenuItems())
    private val bottomMenu = createMenu(createBottomMenuItems())

    init {
        setHeightFull()
        isPadding = false
        addMenuSelectSeparateEvent()
        add(createTopMenuLayer(), createBottomMenuLayer())
    }

    private fun addMenuSelectSeparateEvent() {
        topMenu.addSelectedChangeListener {
            bottomMenu.selectedIndex = UNSELECT_ALL
            topMenu.selectedTab = it.selectedTab
        }
        bottomMenu.addSelectedChangeListener {
            topMenu.selectedIndex = UNSELECT_ALL
            bottomMenu.selectedTab = it.selectedTab
        }
    }

    private fun createTopMenuLayer(): VerticalLayout {
        return VerticalLayout().apply {
            isPadding = false
            add(topMenu)
        }
    }

    private fun createBottomMenuLayer(): VerticalLayout {
        return VerticalLayout().apply {
            isPadding = false
            setHeightFull()
            justifyContentMode = FlexComponent.JustifyContentMode.END
            add(bottomMenu)
        }
    }

    private fun createMenu(list: List<MenuItem>): Tabs {
        return createTabs(list.flatMap { it.toComponents() })
    }

    private fun createTopMenuItems(): List<MenuItem> {
        val recruitments = recruitmentService.findAll()
        return listOf(
            "모집선택".createComboBoxTab(
                recruitments,
                listOf(
                    "과제 관리" of MissionsView::class.java,
                    "선발 과정" of SelectionView::class.java,
                )
            )
        )
    }

    private fun createBottomMenuItems(): List<MenuItem> {
        return listOf(
            createBorderItem(),
            "기수 관리" of TermsView::class.java,
            "모집 관리" of RecruitmentsView::class.java,
            "평가 관리" of EvaluationsView::class.java,
            createBorderItem(),
            "메일 관리" of MailsView::class.java,
            "부정행위자" of CheatersView::class.java,
            "관리자" of MailsView::class.java
        )
    }
}
