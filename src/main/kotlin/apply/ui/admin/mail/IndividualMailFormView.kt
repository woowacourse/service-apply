package apply.ui.admin.mail

import apply.application.ApplicantResponse
import apply.application.ApplicantService
import apply.application.MailTargetService
import apply.ui.admin.BaseLayout
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.data.renderer.Renderer
import com.vaadin.flow.router.Route
import support.views.Title
import support.views.addSortableColumn
import support.views.createErrorButton
import support.views.createNormalButton
import support.views.createPrimaryButton
import support.views.createSearchBar

@Route(value = "personal", layout = BaseLayout::class)
class IndividualMailFormView(
    private val applicantService: ApplicantService,
    mailTargetService: MailTargetService
) : MailFormView(mailTargetService) {
    init {
        add(Title("개별 발송"), createMailForm())
        setWidthFull()
    }

    override fun createReceiverFilter(): Component {
        return createNormalButton("지원자 조회") {
            Dialog().apply {
                width = "800px"
                height = "90%"
                val applicants = HorizontalLayout().apply {
                    setWidthFull()
                }
                add(
                    H2("지원자 정보 검색"),
                    HorizontalLayout(
                        createAddReceivers(),
                        HorizontalLayout(
                            createErrorButton("취소") {
                                close()
                            }
                        ).apply {
                            justifyContentMode = FlexComponent.JustifyContentMode.END
                            defaultHorizontalComponentAlignment = FlexComponent.Alignment.END
                        }
                    ),
                    applicants
                ).apply {
                    setWidthFull()
                }
                open()
            }
        }.apply { isEnabled = true }
    }

    private fun createAddReceivers(): Component {
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
        if (this.receivers.contains(applicantResponse.email)) {
            return createDeleteButton(applicantResponse)
        }

        return createAddButton(applicantResponse)
    }

    private fun createAddButton(applicantResponse: ApplicantResponse): Button {
        return createPrimaryButton("추가") {
            this.receivers.add(applicantResponse.email)
        }.apply {
            isDisableOnClick = true
        }
    }

    private fun createDeleteButton(applicantResponse: ApplicantResponse): Button {
        return createErrorButton("삭제") {
            this.receivers.remove(applicantResponse.email)
        }.apply {
            isDisableOnClick = true
        }
    }
}
