package apply.ui.admin.cheater

import apply.application.ApplicantResponse
import apply.application.ApplicantService
import apply.application.CheaterResponse
import apply.application.CheaterService
import apply.ui.admin.BaseLayout
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.data.provider.DataProvider
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.data.renderer.Renderer
import com.vaadin.flow.router.Route
import org.vaadin.klaudeta.PaginatedGrid
import support.views.addBackEndSortableColumn
import support.views.addBackEndSortableDateTimeColumn
import support.views.createDeleteButtonWithDialog
import support.views.createPrimaryButton
import support.views.createSearchBar
import support.views.toMap

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
        val container = HorizontalLayout()
        return HorizontalLayout(
            createSearchBar {
                container.removeAll()
                val founds = applicantService.findAllByKeyword(it)
                if (founds.isNotEmpty()) {
                    val select = createSelectApplicant(founds)
                    container.add(
                        select,
                        createPrimaryButton("추가") {
                            cheaterService.save(select.value.id)
                            UI.getCurrent().page.reload()
                        }
                    )
                }
            },
            container
        ).apply { setSizeFull() }
    }

    private fun createSelectApplicant(applicants: List<ApplicantResponse>): Select<ApplicantResponse> {
        return Select<ApplicantResponse>().apply {
            setTextRenderer { "${it.name}/${it.email}" }
            setItems(applicants)
        }
    }

    private fun createCheaterGrid(): PaginatedGrid<CheaterResponse> {
        return PaginatedGrid<CheaterResponse>().apply {
            addBackEndSortableColumn("이름", "information.name") { it.applicant.name }
            addBackEndSortableColumn("이메일", "information.email") { it.applicant.email }
            addBackEndSortableDateTimeColumn("등록일", "c.createdDateTime", CheaterResponse::createdDateTime)
            addColumn(createDeleteButtonRenderer()).apply { isAutoWidth = true }
            pageSize = 10
            isMultiSort = true
            dataProvider = DataProvider.fromCallbacks(
                { query ->
                    cheaterService.findAll(
                        query.offset,
                        query.limit,
                        query.sortOrders.toMap()
                    ).stream()
                },
                { cheaterService.count().toInt() }
            )
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
