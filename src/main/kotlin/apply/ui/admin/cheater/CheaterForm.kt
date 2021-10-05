package apply.ui.admin.cheater

import apply.application.UserResponse
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
    private val users: Select<UserResponse> = createItemSelect<UserResponse>().apply {
        setTextRenderer { "${it.name}/${it.email}" }
    }
    private val description: TextArea = TextArea("등록 사유").apply {
        placeholder = "사유를 입력하세요."
    }

    constructor(listener: (String) -> List<UserResponse>) : this() {
        add(createUserSearchBar(listener), description)
        setResponsiveSteps(ResponsiveStep("0", 1))
        drawRequired()
    }

    private fun createUserSearchBar(listener: (String) -> List<UserResponse>): Component {
        val searchBar = createSearchBox("회원 검색") {
            users.setItems(listener(it))
        }
        return HorizontalLayout(searchBar, users).apply {
            defaultVerticalComponentAlignment = FlexComponent.Alignment.END
        }
    }

    override fun bindOrNull(): CheaterData? {
        return bindDefaultOrNull()?.apply {
            email = users.value.email
        }
    }

    override fun fill(data: CheaterData) {
        fillDefault(data)
    }
}
