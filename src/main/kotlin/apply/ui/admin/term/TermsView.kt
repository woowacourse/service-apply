package apply.ui.admin.term

import apply.application.TermResponse
import apply.application.TermService
import apply.domain.term.Term
import apply.ui.admin.BaseLayout
import apply.ui.admin.recruitment.RecruitmentsFormView
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
import support.views.createContrastButton
import support.views.createDeleteButtonWithDialog
import support.views.createPrimaryButton
import support.views.createPrimarySmallButton
import support.views.toDisplayName

@Route(value = "admin/terms", layout = BaseLayout::class)
class TermsView(private val termService: TermService) : VerticalLayout() {
    init {
        add(createTitle(), createButtons(), createGrid())
    }

    private fun createTitle(): Component {
        return HorizontalLayout(H1("기수 관리")).apply {
            setSizeFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        }
    }

    private fun createGrid(): Component {
        return Grid<TermResponse>(10).apply {
            addSortableColumn("기수 명", TermResponse::name)
            addColumn(createButtonRenderer()).apply {
                isAutoWidth = true
                justifyContentMode = FlexComponent.JustifyContentMode.END
            }
            setItems(termService.findAll())
        }
    }

    private fun createButtons(): Component {
        return HorizontalLayout(
            createContrastButton("돌아가기") {
                UI.getCurrent().navigate(RecruitmentsFormView::class.java, NEW_VALUE)
            },
            createPrimaryButton("생성") {
                TermFormDialog(termService, NEW_VALUE.toDisplayName())
            }
        ).apply {
            setSizeFull()
            justifyContentMode = FlexComponent.JustifyContentMode.END
        }
    }

    private fun createButtonRenderer(): Renderer<TermResponse> {
        return ComponentRenderer { it -> createButtons(it) }
    }

    private fun createButtons(term: TermResponse): Component {
        return HorizontalLayout(
            createEditButton(term),
            createDeleteButton(term)
        )
    }

    private fun createEditButton(term: TermResponse): Component {
        return createPrimarySmallButton("수정") {
            TermFormDialog(termService, EDIT_VALUE.toDisplayName(), term)
        }.apply {
            if (term.id == Term.SINGLE.id) {
                isEnabled = false
            }
        }
    }

    private fun createDeleteButton(term: TermResponse): Button {
        return createDeleteButtonWithDialog("모집을 삭제하시겠습니까?") {
            // TODO : delete
        }.apply {
            if (term.id == Term.SINGLE.id) {
                isEnabled = false
            }
        }
    }
}
