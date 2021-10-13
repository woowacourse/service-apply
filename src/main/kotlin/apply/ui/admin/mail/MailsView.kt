package apply.ui.admin.mail

import apply.application.MailHistoryService
import apply.application.mail.MailData
import apply.ui.admin.BaseLayout
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
import support.views.NEW_VALUE
import support.views.addSortableColumn
import support.views.addSortableDateTimeColumn
import support.views.createPrimaryButton
import support.views.createPrimarySmallButton

@Route(value = "admin/mails", layout = BaseLayout::class)
class MailsView(
    private val mailHistoryService: MailHistoryService
) : VerticalLayout() {
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
                UI.getCurrent().navigate(MailsFormView::class.java, NEW_VALUE)
            }
        ).apply {
            setSizeFull()
            justifyContentMode = FlexComponent.JustifyContentMode.END
        }
    }

    private fun createGrid(): Component {
        return Grid<MailData>(10).apply {
            addSortableColumn("메일 제목", MailData::subject)
            addSortableDateTimeColumn("보낸 시간", MailData::sentTime)
            addSortableColumn("받는사람 수") { "${it.recipients.size}명" }
            addColumn(createButtonRenderer()).apply { isAutoWidth = true }
            setItems(mailHistoryService.findAll())
        }
    }

    private fun createButtonRenderer(): Renderer<MailData> {
        return ComponentRenderer<Component, MailData> { response -> createDetailButton(response) }
    }

    private fun createDetailButton(mailData: MailData): Component {
        return createPrimarySmallButton("상세 보기") {
            UI.getCurrent().navigate(MailsFormView::class.java, "${mailData.id}/$EDIT_VALUE")
        }
    }
}
