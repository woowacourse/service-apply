package apply.ui.admin.recruitment

import apply.application.RecruitmentService
import apply.ui.admin.BaseLayout
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.Route
import support.createContrastButton
import support.createPrimaryButton

@Route(value = "admin/recruitments/form", layout = BaseLayout::class)
class RecruitmentsFormView(private val recruitmentService: RecruitmentService) : VerticalLayout() {
    private val recruitmentForm: RecruitmentForm = RecruitmentForm()

    init {
        add(createTitle(), recruitmentForm, createButtons())
    }

    private fun createTitle(): Component {
        return HorizontalLayout(H1("모집 생성")).apply {
            setSizeFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        }
    }

    private fun createButtons(): Component {
        return HorizontalLayout(createAddButton(), createCancelButton()).apply {
            setSizeFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        }
    }

    private fun createAddButton(): Button {
        return createPrimaryButton("생성") {
            recruitmentForm.bindOrNull()?.let {
                recruitmentService.save(it)
                UI.getCurrent().navigate(RecruitmentsView::class.java)
            }
        }
    }

    private fun createCancelButton(): Button {
        return createContrastButton("취소") {
            UI.getCurrent().navigate(RecruitmentsView::class.java)
        }
    }
}
