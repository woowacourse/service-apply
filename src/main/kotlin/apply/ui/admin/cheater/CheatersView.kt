package apply.ui.admin.cheater

import apply.application.ApplicantService
import apply.application.CheaterResponse
import apply.application.CheaterService
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
import support.views.addSortableColumn
import support.views.addSortableDateTimeColumn
import support.views.createDeleteButtonWithDialog
import support.views.createPrimaryButton

@Route(value = "admin/cheaters", layout = BaseLayout::class)
class CheatersView(
    private val applicantService: ApplicantService,
    private val cheaterService: CheaterService
) : VerticalLayout() {
    init {
        add(createTitle(), createAddCheater(), createCheaterGrid())
    }

    private fun createTitle(): Component {
        return HorizontalLayout(H1("부정 행위자")).apply {
            setSizeFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        }
    }

    private fun createAddCheater(): Component {
        return HorizontalLayout(
            createPrimaryButton("추가") {
                CheaterRegistrationFormDialog(applicantService, cheaterService) {
                    UI.getCurrent().page.reload()
                }
            }
        ).apply {
            justifyContentMode = FlexComponent.JustifyContentMode.END
            setSizeFull()
        }
    }

    private fun createCheaterGrid(): Grid<CheaterResponse> {
        return Grid<CheaterResponse>(10).apply {
            addSortableColumn("이름") { it.name ?: "(이름 없음)" }
            addSortableColumn("이메일") { it.email }
            addSortableDateTimeColumn("등록일", CheaterResponse::createdDateTime)
            addSortableColumn("설명") { it.description }
            addColumn(createDeleteButtonRenderer()).apply { isAutoWidth = true }
            setItems(cheaterService.findAll())
        }
    }

    private fun createDeleteButtonRenderer(): Renderer<CheaterResponse> {
        return ComponentRenderer<Component, CheaterResponse> { cheater ->
            createDeleteButtonWithDialog("부정 행위자를 삭제하시겠습니까?") {
                cheaterService.deleteById(cheater.id)
            }
        }
    }
}
