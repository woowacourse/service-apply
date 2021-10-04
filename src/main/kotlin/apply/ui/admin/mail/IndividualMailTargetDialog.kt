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
import support.views.createPrimarySmallButton
import support.views.createSearchBox

class IndividualMailTargetDialog(
    private val applicantService: ApplicantService,
    private val accept: (MailTargetResponse) -> Unit
) : Dialog() {
    private val mailTargetsGrid: Grid<ApplicantResponse> = createMailTargetsGrid()

    init {
        add(H2("개별 불러오기"), createSearchFilter(), mailTargetsGrid, createButtons())
        width = "900px"
        height = "70%"
        open()
    }

    private fun createSearchFilter(): Component {
        return HorizontalLayout(
            createSearchBox { mailTargetsGrid.setItems(applicantService.findAllByKeyword(it)) }
        ).apply {
            element.style.set("margin-top", "10px")
            element.style.set("margin-bottom", "10px")
        }
    }

    private fun createMailTargetsGrid(): Grid<ApplicantResponse> {
        return Grid<ApplicantResponse>(10).apply {
            addSortableColumn("이름", ApplicantResponse::name)
            addSortableColumn("이메일", ApplicantResponse::email)
            addColumn(createAddButton()).apply { isAutoWidth = true }
        }
    }

    private fun createAddButton(): Renderer<ApplicantResponse> {
        return ComponentRenderer<Component, ApplicantResponse> { applicantResponse ->
            createPrimarySmallButton("추가") {
                accept(MailTargetResponse(applicantResponse))
            }.apply {
                isDisableOnClick = true
            }
        }
    }

    private fun createButtons(): Component {
        return HorizontalLayout(createCancelButton()).apply {
            setSizeFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
            element.style.set("margin-top", "10px")
        }
    }

    private fun createCancelButton(): Button {
        return createContrastButton("취소") {
            close()
        }
    }
}
