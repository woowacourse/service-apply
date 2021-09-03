package apply.ui.admin.cheater

import apply.application.ApplicantResponse
import apply.application.ApplicantService
import apply.application.CheaterResponse
import apply.application.CheaterService
import apply.ui.admin.BaseLayout
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.Text
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.data.renderer.Renderer
import com.vaadin.flow.router.Route
import support.views.addSortableColumn
import support.views.addSortableDateTimeColumn
import support.views.createDeleteButtonWithDialog
import support.views.createPrimaryButton
import support.views.createPrimarySmallButton
import support.views.createSearchBar

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
                            cheaterService.save(select.value.email)
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

    private fun createCheaterGrid(): Grid<CheaterResponse> {
        return Grid<CheaterResponse>(10).apply {
            addSortableColumn("이름") { it.applicant.name }
            addSortableColumn("이메일") { it.applicant.email }
            addSortableDateTimeColumn("등록일", CheaterResponse::createdDateTime)
            addColumn(createDeleteButtonRenderer()).apply { isAutoWidth = true }
            addColumn(createDetailInformationButtonRenderer()).apply { isAutoWidth = true }
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

    private fun createDetailInformationButtonRenderer(): Renderer<CheaterResponse> {
        return ComponentRenderer<Component, CheaterResponse> { cheater ->
            createPrimarySmallButton("상세 내용 조회") {
                createInformationModal(cheater)
            }
        }
    }

    private fun createInformationModal(cheater: CheaterResponse) {
        Dialog().apply {
            add(
                Text("description of cheater " + cheater.id)
            )
            add(
                HorizontalLayout(
                    Div(
                        Button("confirmed") {
                            this.close()
                        }
                    )
                ).apply {
                    justifyContentMode = FlexComponent.JustifyContentMode.CENTER
                }
            )
        }.open()
    }
}
