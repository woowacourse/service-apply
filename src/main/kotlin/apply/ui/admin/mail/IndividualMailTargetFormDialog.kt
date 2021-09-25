package apply.ui.admin.mail

import apply.application.ApplicantResponse
import apply.application.ApplicantService
import apply.application.MailTargetResponse
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.data.renderer.Renderer
import support.views.addSortableColumn
import support.views.createContrastButton
import support.views.createPrimaryButton
import support.views.createSearchBar

class IndividualMailTargetFormDialog(
    private val applicantService: ApplicantService,
    private val reloadComponent: (MailTargetResponse) -> Unit
) : Dialog() {
    private val currentMailTargetsGrid: Grid<ApplicantResponse> = createGrid()

    init {
        add(
            H2("지원자 정보 조회"),
            createRecipientFilter(),
            currentMailTargetsGrid
        ).apply {
            setWidthFull()
        }
        width = "900px"
        height = "70%"
        open()
    }

    private fun createGrid(applicantResponse: List<ApplicantResponse> = emptyList()): Grid<ApplicantResponse> {
        return Grid<ApplicantResponse>(10).apply {
            addSortableColumn("이름", ApplicantResponse::name)
            addSortableColumn("이메일", ApplicantResponse::email)
            addColumn(createEditAndDeleteButton()).apply { isAutoWidth = true }
            setItems(applicantResponse)
        }
    }

    private fun createEditAndDeleteButton(): Renderer<ApplicantResponse> {
        return ComponentRenderer<Component, ApplicantResponse> { applicantResponse ->
            HorizontalLayout(createTargetAddButton(applicantResponse))
        }
    }

    private fun createTargetAddButton(applicantResponse: ApplicantResponse): Button {
        return createPrimaryButton("추가") {
            reloadComponent(MailTargetResponse(applicantResponse))
        }.apply {
            isDisableOnClick = true
        }
    }

    private fun createRecipientFilter(): Component {
        return HorizontalLayout(
            createSearchBar {
                val founds = applicantService.findAllByKeyword(it)
                currentMailTargetsGrid.apply {
                    setItems(founds)
                }
            },
            createCancelButton()
        ).apply {
            justifyContentMode = FlexComponent.JustifyContentMode.START
            element.style.set("margin-top", "10px")
            element.style.set("margin-bottom", "10px")
        }
    }

    private fun createCancelButton(): Button {
        return createContrastButton("취소") {
            close()
        }
    }
}
