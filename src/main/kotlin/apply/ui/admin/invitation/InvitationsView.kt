package apply.ui.admin.invitation

import apply.application.InvitationResponse
import apply.application.InvitationService
import apply.ui.admin.BaseLayout
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.html.Label
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.data.renderer.Renderer
import com.vaadin.flow.router.Route
import support.views.addSortableColumn
import support.views.addSortableDateTimeColumn
import support.views.createErrorSmallButton
import support.views.createPrimaryButton
import support.views.createPrimarySmallButton

@Route(value = "admin/invitations", layout = BaseLayout::class)
class InvitationsView(
    private val invitationService: InvitationService,
) : VerticalLayout() {
    init {
        setSizeFull()
        add(createTitle(), createAcceptAllButton(), createGrid())
    }

    private fun createTitle(): Component {
        return HorizontalLayout(H1("초대 관리")).apply {
            setWidthFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        }
    }

    private fun createAcceptAllButton(): Component {
        return HorizontalLayout(
            createPrimaryButton("조건부 수락") {
                InviteAcceptanceDialog(invitationService).open()
            }
        ).apply {
            setWidthFull()
            justifyContentMode = FlexComponent.JustifyContentMode.END
        }
    }

    private fun createGrid(): Component {
        return Grid<InvitationResponse>(10).apply {
            addSortableColumn("GitHub 사용자 이름", InvitationResponse::githubUsername)
            addSortableColumn("저장소 이름", InvitationResponse::repositoryName)
            addSortableColumn("저장소 전체 이름", InvitationResponse::repositoryFullName)
            addSortableDateTimeColumn("초대 일시", InvitationResponse::invitationDateTime)
            addColumn(createButtonRenderer()).apply { isAutoWidth = true }
            invitationService.findAll().also {
                setItems(it)
                columns.first().setFooter("총 ${it.size}개")
            }
        }
    }

    private fun createButtonRenderer(): Renderer<InvitationResponse> {
        return ComponentRenderer { it -> createButtons(it) }
    }

    private fun createButtons(invitation: InvitationResponse): Component {
        return HorizontalLayout(
            createAcceptButton(invitation),
            createDeclineButton(invitation),
        )
    }

    private fun createAcceptButton(invitation: InvitationResponse): Component {
        return createPrimarySmallButton("수락") {
            invitationService.accept(invitation.id)
            UI.getCurrent().page.reload()
        }
    }

    private fun createDeclineButton(invitation: InvitationResponse): Component {
        return createErrorSmallButton("거절") {
            invitationService.decline(invitation.id)
            UI.getCurrent().page.reload()
        }
    }
}
