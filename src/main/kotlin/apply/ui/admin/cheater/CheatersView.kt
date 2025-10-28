package apply.ui.admin.cheater

import apply.application.CheaterResponse
import apply.application.CheaterService
import apply.application.MemberService
import apply.ui.admin.BaseLayout
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.data.renderer.Renderer
import com.vaadin.flow.router.Route
import support.views.NO_NAME
import support.views.Title
import support.views.addSortableColumn
import support.views.addSortableDateTimeColumn
import support.views.createDeleteButtonWithDialog
import support.views.createPrimaryButton

@Route(value = "admin/cheaters", layout = BaseLayout::class)
class CheatersView(
    private val memberService: MemberService,
    private val cheaterService: CheaterService,
) : VerticalLayout() {
    init {
        setSizeFull()
        add(createTitle(), createToolbar(), createGrid())
    }

    private fun createTitle(): Component = Title("부정행위자")

    private fun createToolbar(): Component {
        return HorizontalLayout(
            createPrimaryButton("추가") {
                CheaterFormDialog(memberService, cheaterService) {
                    UI.getCurrent().page.reload()
                }
            }
        ).apply {
            setWidthFull()
            justifyContentMode = FlexComponent.JustifyContentMode.END
        }
    }

    private fun createGrid(): Component {
        return Grid<CheaterResponse>(10).apply {
            addSortableColumn("이름") { it.name ?: NO_NAME }
            addSortableColumn("이메일") { it.email }
            addSortableDateTimeColumn("등록일", CheaterResponse::createdDateTime)
            addSortableColumn("설명") { it.description }
            addColumn(createDeleteButtonRenderer()).apply { isAutoWidth = true }
            setItems(cheaterService.findAll())
        }
    }

    private fun createDeleteButtonRenderer(): Renderer<CheaterResponse> {
        return ComponentRenderer { cheater ->
            createDeleteButtonWithDialog("부정행위자를 삭제하시겠습니까?") {
                cheaterService.deleteById(cheater.id)
            }
        }
    }
}
