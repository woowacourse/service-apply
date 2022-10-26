package apply.ui.admin

import apply.application.RecruitmentService
import apply.ui.admin.cheater.CheatersView
import apply.ui.admin.evaluation.EvaluationsView
import apply.ui.admin.mail.MailsView
import apply.ui.admin.mission.MissionsView
import apply.ui.admin.recruitment.RecruitmentsView
import apply.ui.admin.selections.SelectionView
import apply.ui.admin.term.TermsView
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import support.views.createTabs

class MenuLayout(
    private val recruitmentService: RecruitmentService
) : VerticalLayout() {

    init {
        setHeightFull()
        isPadding = false
        add(createTopMenuLayer(), createBottomMenuLayer())
    }

    private fun createTopMenuLayer(): VerticalLayout {
        return VerticalLayout().apply {
            isPadding = false
            add(createMenu(createTopMenuItems()))
        }
    }

    private fun createBottomMenuLayer(): VerticalLayout {
        return VerticalLayout(createMenu(createBottomMenuItems())).apply {
            isPadding = false
            setHeightFull()
            justifyContentMode = FlexComponent.JustifyContentMode.END
        }
    }

    private fun createMenu(list: List<MenuItem>): Component {
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
            "부정행위자" of CheatersView::class.java,
            "메일 관리" of MailsView::class.java,
            "관리자 관리" of MailsView::class.java
        )
    }
}
