package apply.ui.admin.term

import apply.application.TermResponse
import apply.application.TermService
import apply.domain.term.Term
import apply.ui.admin.BaseLayout
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
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
import support.views.createDeleteButtonWithDialog
import support.views.createPrimaryButton
import support.views.createPrimarySmallButton
import support.views.toDisplayName

@Route(value = "admin/terms", layout = BaseLayout::class)
class TermsView(private val termService: TermService) : VerticalLayout() {
    init {
        add(createTitle(), createButton(), createGrid())
    }

    private fun createTitle(): Component {
        return HorizontalLayout(H1("기수 관리")).apply {
            setSizeFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        }
    }

    private fun createButton(): Component {
        return HorizontalLayout(
            createPrimaryButton("생성") {
                TermFormDialog(termService, NEW_VALUE.toDisplayName()) {
                    UI.getCurrent().page.reload()
                }
            }
        ).apply {
            setSizeFull()
            justifyContentMode = FlexComponent.JustifyContentMode.END
        }
    }

    private fun createGrid(): Component {
        return Grid<TermResponse>(10).apply {
            addSortableColumn("기수명", TermResponse::name)
            addColumn(createButtonRenderer()).apply { isAutoWidth = true }
            setItems(termService.findAll())
        }
    }

    private fun createButtonRenderer(): Renderer<TermResponse> {
        return ComponentRenderer { it -> createButtons(it) }
    }

    private fun createButtons(term: TermResponse): Component {
        val block: Button.() -> Unit = { isEnabled = term.id != Term.SINGLE.id }
        return HorizontalLayout(
            createEditButton(term).apply(block),
            createDeleteButton(term).apply(block)
        )
    }

    private fun createEditButton(term: TermResponse): Button {
        return createPrimarySmallButton("수정") {
            TermFormDialog(termService, EDIT_VALUE.toDisplayName(), term) {
                UI.getCurrent().page.reload()
            }
        }
    }

    private fun createDeleteButton(term: TermResponse): Button {
        return createDeleteButtonWithDialog("기수를 삭제하시겠습니까?") {
            termService.deleteById(term.id)
        }
    }
}
