package apply.ui.admin.administrator

import apply.application.AdministratorResponse
import apply.application.AdministratorService
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
import support.views.NEW_VALUE
import support.views.addSortableColumn
import support.views.createDeleteButtonWithDialog
import support.views.createPrimaryButton
import support.views.createPrimarySmallButton
import support.views.toDisplayName

@Route(value = "admin/administrators", layout = BaseLayout::class)
class AdministratorsView(private val administratorService: AdministratorService) : VerticalLayout() {
    init {
        add(createTitle(), createButton(), createGrid())
    }

    private fun createTitle(): Component {
        return HorizontalLayout(H1("관리자")).apply {
            setSizeFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        }
    }

    private fun createButton(): Component {
        return HorizontalLayout(
            createPrimaryButton("생성") {
                AdministratorFormDialog(administratorService, NEW_VALUE.toDisplayName()) {
                    UI.getCurrent().page.reload()
                }
            }
        ).apply {
            setSizeFull()
            justifyContentMode = FlexComponent.JustifyContentMode.END
        }
    }

    private fun createGrid(): Component {
        return Grid<AdministratorResponse>(10).apply {
            addSortableColumn("관리자명", AdministratorResponse::name)
            addSortableColumn("관리자 ID", AdministratorResponse::username)
            addColumn(createEditAndDeleteButton()).apply { isAutoWidth = true }
            setItems(administratorService.findAll())
        }
    }

    private fun createEditAndDeleteButton(): Renderer<AdministratorResponse> {
        return ComponentRenderer { administratorResponse ->
            HorizontalLayout(
                // TODO : implement edit and delete logic
                createPrimarySmallButton("수정") {
                    // UI.getCurrent().navigate(AdministratorsFormView::class.java, "${administratorResponse.id}/$EDIT_VALUE")
                },
                createDeleteButtonWithDialog("관리자를 삭제하시겠습니까?") {
                    // administratorService.deleteById(administratorResponse.id)
                }
            ).apply {
                justifyContentMode = FlexComponent.JustifyContentMode.END
            }
        }
    }
}
