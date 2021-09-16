package apply.ui.admin.mail

import apply.application.ApplicantResponse
import apply.application.ApplicantService
import apply.ui.admin.BaseLayout
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.Key
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
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
    private val applicantService: ApplicantService
) : MailFormView() {
    init {
        add(Title("개별 발송"), createMailForm())
        setWidthFull()
    }

    override fun createRecipientFilter(): Component {
        return HorizontalLayout(
            createDirectInsertTargetComponent {
                if (it.isNotBlank()) {
                    addRecipientComponent(it)
                }
            },
            createSearchTargetComponent()
        )
    }

    private fun createDirectInsertTargetComponent(eventListener: (name: String) -> Unit): Div {
        val mailTarget = TextField().apply {
            addKeyDownListener(
                Key.ENTER,
                {
                    eventListener(this.value)
                    this.value = ""
                }
            )
        }

        return Div(
            HorizontalLayout(
                mailTarget,
                Button(Icon(VaadinIcon.ENTER_ARROW)) {
                    eventListener(mailTarget.value)
                    mailTarget.value = ""
                }
            )
        )
    }

    private fun createSearchTargetComponent(): Button {
        return createNormalButton("지원자 조회") {
            Dialog().apply {
                width = "800px"
                height = "90%"
                add(
                    H2("지원자 정보 검색"),
                    HorizontalLayout(
                        createAddRecipients(),
                        HorizontalLayout(
                            createErrorButton("취소") {
                                close()
                            }
                        ).apply {
                            justifyContentMode = FlexComponent.JustifyContentMode.END
                            defaultHorizontalComponentAlignment = FlexComponent.Alignment.END
                        }
                    )
                ).apply {
                    setWidthFull()
                }
                open()
            }
        }.apply { isEnabled = true }
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
        if (this.recipients.contains(applicantResponse.email)) {
            return createTargetDeleteButton(applicantResponse)
        }

        return createTargetAddButton(applicantResponse)
    }

    private fun createTargetAddButton(applicantResponse: ApplicantResponse): Button {
        return createPrimaryButton("추가") {
            addRecipientComponent(applicantResponse.email)
        }.apply {
            isDisableOnClick = true
        }
    }

    private fun createTargetDeleteButton(applicantResponse: ApplicantResponse): Button {
        return createErrorButton("삭제") {
            removeRecipientComponent(applicantResponse.email)
        }.apply {
            isDisableOnClick = true
        }
    }
}
