package apply.ui.admin.invitation

import apply.ui.admin.BaseLayout
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.data.renderer.Renderer
import com.vaadin.flow.router.Route
import support.views.addSortableColumn
import support.views.addSortableDateTimeColumn
import support.views.createErrorSmallButton
import support.views.createPrimarySmallButton
import java.time.LocalDateTime

@Route(value = "admin/invitations", layout = BaseLayout::class)
class InvitationsView : VerticalLayout() {
    init {
        add(createTitle(), createGrid())
    }

    private fun createTitle(): Component {
        return HorizontalLayout(H1("초대 관리")).apply {
            setSizeFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        }
    }

    private fun createGrid(): Component {
        val invitations = listOf(
            InvitationResponse(
                id = 1L,
                githubUsername = "woowahan-pjs",
                repositoryName = "nextstep_test",
                repositoryFullName = "woowahan-pjs/nextstep_test",
                invitationDateTime = LocalDateTime.now(),
                expired = false
            ),
        )
        return Grid<InvitationResponse>(10).apply {
            addSortableColumn("GitHub 사용자 이름", InvitationResponse::githubUsername)
            addSortableColumn("저장소 이름", InvitationResponse::repositoryName)
            addSortableColumn("저장소 전체 이름", InvitationResponse::repositoryFullName)
            addSortableDateTimeColumn("초대 일시", InvitationResponse::invitationDateTime)
            addColumn(createButtonRenderer()).apply { isAutoWidth = true }
            setItems(invitations)
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
        }
    }

    private fun createDeclineButton(invitation: InvitationResponse): Component {
        return createErrorSmallButton("거절") {
        }
    }
}

data class InvitationResponse(
    val id: Long,
    val githubUsername: String,
    val repositoryName: String,
    val repositoryFullName: String,
    val invitationDateTime: LocalDateTime,
    val expired: Boolean,
)
