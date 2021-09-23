package apply.ui.admin.mail

import apply.application.ApplicantResponse
import apply.application.ApplicantService
import apply.application.MailTargetResponse
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.data.renderer.Renderer
import support.views.addSortableColumn
import support.views.createPrimaryButton
import support.views.createSearchBar

class IndividualMailTargetFormDialog(
    private val applicantService: ApplicantService,
    private val reloadComponent: (MailTargetResponse) -> Unit
) : Dialog() {
    init {
        width = "800px"
        height = "90%"
        add(H2("지원자 정보 조회"), HorizontalLayout(createAddRecipients())).apply {
            setWidthFull()
        }
        open()
    }

    private fun createAddRecipients(): Component {
        val container = VerticalLayout()
        return VerticalLayout(
            createSearchBar {
                container.removeAll()
                val founds = applicantService.findAllByKeyword(it)
                if (founds.isNotEmpty()) {
                    container.add(createGrid(founds))
                }
            },
            container
        )
    }

    private fun createGrid(applicantResponse: List<ApplicantResponse>): Component {
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
}
