package apply.ui.admin.cheater

import apply.application.ApplicantResponse
import apply.application.CheaterData
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.component.textfield.TextArea
import support.views.BindingFormLayout
import support.views.createSearchBar

class CheaterForm() : BindingFormLayout<CheaterData>(CheaterData::class) {
    private val applicants: Select<ApplicantResponse> = Select<ApplicantResponse>().apply {
        setTextRenderer { "${it.name}/${it.email}" }
    }
    private val description: TextArea = TextArea("등록 사유").apply {
        placeholder = "사유를 입력하세요"
        setWidthFull()
    }

    init {
        add(description).also {
            this.children.forEach {
                setColspan(it, 2)
            }
        }
        drawRequired()
    }

    constructor(
        listener: (keyword: String) -> List<ApplicantResponse>
    ) : this() {
        val searchBar = createApplicantSearchBar(listener)
        addComponentAsFirst(searchBar)
    }

    private fun createApplicantSearchBar(listener: (keyword: String) -> List<ApplicantResponse>): Component {
        val container = HorizontalLayout()
        return HorizontalLayout(
            createSearchBar("지원자 검색") {
                container.removeAll()
                val founds = listener(it)
                if (founds.isNotEmpty()) {
                    applicants.setItems(listener(it))
                    container.add(applicants)
                }
            }.apply {
                children.forEach {
                    if (it is HorizontalLayout)
                        it.apply { defaultVerticalComponentAlignment = FlexComponent.Alignment.END }
                }
            },
            container
        ).apply {
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
