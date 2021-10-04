package apply.ui.admin.cheater

import apply.application.ApplicantResponse
import apply.application.CheaterData
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.component.textfield.TextArea
import support.views.BindingFormLayout
import support.views.createItemSelect
import support.views.createSearchBox

class CheaterForm() : BindingFormLayout<CheaterData>(CheaterData::class) {
    private val applicants: Select<ApplicantResponse> = createItemSelect<ApplicantResponse>().apply {
        setTextRenderer { "${it.name}/${it.email}" }
    }
    private val description: TextArea = TextArea("등록 사유").apply {
        placeholder = "사유를 입력하세요."
    }

    constructor(listener: (String) -> List<ApplicantResponse>) : this() {
        add(createApplicantSearchBar(listener), description)
        setResponsiveSteps(ResponsiveStep("0", 1))
        drawRequired()
    }

    private fun createApplicantSearchBar(listener: (String) -> List<ApplicantResponse>): Component {
        val searchBar = createSearchBox("회원 검색") {
            applicants.setItems(listener(it))
        }
        return HorizontalLayout(searchBar, applicants).apply {
            defaultVerticalComponentAlignment = FlexComponent.Alignment.END
        }
    }

    override fun bindOrNull(): CheaterData? {
        return bindDefaultOrNull()?.apply {
            email = applicants.value.email
        }
    }

    override fun fill(data: CheaterData) {
        fillDefault(data)
    }
}
