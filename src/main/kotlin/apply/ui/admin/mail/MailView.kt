package apply.ui.admin.mail

import apply.application.MailTargetService
import apply.application.RecruitmentResponse
import apply.application.mail.MailHistoryData
import apply.ui.admin.BaseLayout
import apply.ui.admin.recruitment.RecruitmentsFormView
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.data.renderer.Renderer
import com.vaadin.flow.router.Route
import support.views.EDIT_VALUE
import support.views.addSortableColumn
import support.views.createPrimaryButton
import support.views.createPrimarySmallButton

// todo: 페이지 만들기
@Route(value = "admin/mail-history", layout = BaseLayout::class)
class MailView(private val mailTargetService: MailTargetService) : VerticalLayout() {
    init {
        add(createTitle(), createButton(), createGrid())
    }

    private fun createTitle(): Component {
        return HorizontalLayout(H1("메일 관리")).apply {
            setSizeFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        }
    }

    private fun createButton(): Component {
        return HorizontalLayout(
            createPrimaryButton("메일 보내기") {
                UI.getCurrent().navigate(MailFormView::class.java)
            }
        ).apply {
            setSizeFull()
            justifyContentMode = FlexComponent.JustifyContentMode.END
        }
    }

    private fun createGrid(): Component {
        // todo: 수정
        return Grid<MailHistoryData>(10).apply {
            addSortableColumn("메일 제목", MailHistoryData::subject)
            addSortableColumn("보낸 시간", MailHistoryData::sentTime)
            addSortableColumn("받은 사람 수", MailHistoryData::recipientsCount)
            // addColumn(createButtonRenderer()).apply { isAutoWidth = true }
            //  setItems(mailTargetService.findAll())
        }
    }

    // todo
    private fun createButtonRenderer(): Renderer<RecruitmentResponse> {
        return ComponentRenderer<Component, RecruitmentResponse> { it -> createButtons(it) }
    }

    private fun createButtons(recruitment: RecruitmentResponse): Component {
        return HorizontalLayout(
            createEditButton(recruitment)
        )
    }

    private fun createEditButton(recruitment: RecruitmentResponse): Component {
        return createPrimarySmallButton("수정") {
            UI.getCurrent().navigate(RecruitmentsFormView::class.java, "${recruitment.id}/$EDIT_VALUE")
        }
    }
}
