package apply.ui.admin

import apply.application.ApplicantService
import apply.application.CheaterService
import apply.domain.applicant.Applicant
import apply.domain.cheater.Cheater
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.data.renderer.Renderer
import com.vaadin.flow.router.Route
import support.addSortableColumn
import support.addSortableDateTimeColumn
import support.createDeleteButtonWithDialog
import support.createSearchBar

@Route(value = "admin/cheater", layout = BaseLayout::class)
class CheaterView(
    private val applicantService: ApplicantService,
    private val cheaterService: CheaterService
) : VerticalLayout() {
    private val cheaterGrid: Grid<Cheater> = createCheaterGrid()

    init {
        add(createTitle(), createAddCheater(), cheaterGrid)
    }

    private fun createTitle(): Component {
        return HorizontalLayout(H1("부정 행위자")).apply {
            setSizeFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        }
    }

    private fun createAddCheater(): Component {
        val container = HorizontalLayout()
        return HorizontalLayout(
            createSearchBar {
                container.removeAll()
                val founds = applicantService.findByValue(it)
                if (founds.isNotEmpty()) {
                    val select = createSelectApplicant(founds)
                    container.add(
                        select,
                        Button("추가") {
                            cheaterService.save(select.value)
                            cheaterGrid.setItems(cheaterService.findAll())
                        }
                    )
                }
            },
            container
        ).apply { setSizeFull() }
    }

    private fun createSelectApplicant(applicants: List<Applicant>): Select<Applicant> {
        return Select<Applicant>().apply {
            setTextRenderer { "${it.name}/${it.email}" }
            setItems(applicants)
        }
    }

    private fun createCheaterGrid(): Grid<Cheater> {
        return Grid<Cheater>(10).apply {
            addSortableColumn("이름") { it.applicant.name }
            addSortableColumn("이메일") { it.applicant.email }
            addSortableDateTimeColumn("등록일") { it.createdDateTime }
            addColumn(createDeleteButtonRenderer()).apply { isAutoWidth = true }
            setItems(cheaterService.findAll())
        }
    }

    private fun createDeleteButtonRenderer(): Renderer<Cheater> {
        return ComponentRenderer<Component, Cheater> { cheater ->
            createDeleteButtonWithDialog("부정 행위자를 삭제하시겠습니까?") {
                cheaterService.delete(cheater)
            }
        }
    }
}
