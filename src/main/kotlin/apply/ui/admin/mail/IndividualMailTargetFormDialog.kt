package apply.ui.admin.mail

import apply.application.ApplicantResponse
import apply.application.ApplicantService
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
import support.views.createErrorButton
import support.views.createPrimaryButton
import support.views.createSearchBar

class IndividualMailTargetFormDialog(
    private val applicantService: ApplicantService,
    recipients: List<String>,
    reloadComponent: (evaluationTargets: List<String>) -> Unit
) : Dialog() {
    private val additionalRecipients: MutableList<String> = recipients.toMutableList()

    init {
        width = "800px"
        height = "90%"
        add(
            H2("지원자 정보 조회"),
            HorizontalLayout(
                createAddRecipients(),
                HorizontalLayout(
                    createPrimaryButton("추가") {
                        reloadComponent(additionalRecipients)
                        close()
                    },
                    createErrorButton("취소") {
                        close()
                    }
                )
            )
        ).apply {
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
                    container.add(
                        createGrid(founds)
                    )
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
            HorizontalLayout(
                createAddOrDeleteButton(applicantResponse)
            )
        }
    }

    private fun createAddOrDeleteButton(applicantResponse: ApplicantResponse): Component {
        val email = applicantResponse.email
        if (this.additionalRecipients.contains(email)) {
            return createTargetDeleteButton(applicantResponse)
        }

        return createTargetAddButton(applicantResponse)
    }

    private fun createTargetDeleteButton(applicantResponse: ApplicantResponse): Button {
        return createErrorButton("삭제") {
            this.additionalRecipients.remove(applicantResponse.email)
        }.apply {
            isDisableOnClick = true
        }
    }

    private fun createTargetAddButton(applicantResponse: ApplicantResponse): Button {
        return createPrimaryButton("추가") {
            this.additionalRecipients.add(applicantResponse.email)
        }.apply {
            isDisableOnClick = true
        }
    }
}
