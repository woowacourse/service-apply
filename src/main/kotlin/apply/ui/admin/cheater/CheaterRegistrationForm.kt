package apply.ui.admin.cheater

import apply.application.ApplicantResponse
import apply.application.ApplicantService
import apply.application.CheaterData
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.component.textfield.TextArea

class CheaterRegistrationForm(
    val applicantService: ApplicantService
) : FormLayout() {
    private val description: TextArea = TextArea("등록 사유").apply {
        placeholder = "사유를 입력하세요"
        setWidthFull()
    }
    private var selectApplicant: ApplicantResponse? = null
    val cheater: CheaterData
        get() = CheaterData(selectApplicant!!.email, description.value)

    init {
        add(createSearchBar(), description).also {
            this.children.forEach {
                setColspan(it, 2)
            }
        }
    }

    private fun createSearchBar(): Component {
        val container = HorizontalLayout()
        return HorizontalLayout(
            support.views.createSearchBar("지원자 검색") {
                container.removeAll()
                val founds = applicantService.findAllByKeyword(it)
                if (founds.isNotEmpty()) {
                    val select = createSelectApplicant(founds)
                    container.add(select)
                }
            },
            container
        ).apply {
            defaultVerticalComponentAlignment = FlexComponent.Alignment.END
        }
    }

    private fun createSelectApplicant(applicants: List<ApplicantResponse>): Select<ApplicantResponse> {
        return Select<ApplicantResponse>().apply {
            setTextRenderer { "${it.name}/${it.email}" }
            setItems(applicants)
        }.apply {
            addValueChangeListener {
                selectApplicant = it.value
            }
        }
    }
}
