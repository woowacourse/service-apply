package apply.ui.admin.recruitment

import apply.application.RecruitmentService
import apply.ui.admin.BaseLayout
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.BeforeEvent
import com.vaadin.flow.router.HasUrlParameter
import com.vaadin.flow.router.OptionalParameter
import com.vaadin.flow.router.Route
import support.views.Title
import support.views.createContrastButton
import support.views.createPrimaryButton

@Route(value = "admin/recruitments/form", layout = BaseLayout::class)
class RecruitmentsFormView(
    private val recruitmentService: RecruitmentService
) : VerticalLayout(), HasUrlParameter<Long> {
    private val title: Title = Title("모집 생성")
    private val recruitmentForm: RecruitmentForm = RecruitmentForm()
    private val submitButton: Button = createSubmitButton()
    private val cancelButton: Button = createCancelButton()

    init {
        add(title, recruitmentForm, createButtons())
    }

    override fun setParameter(event: BeforeEvent, @OptionalParameter parameter: Long?) {
        if (parameter != null) {
            title.text = "모집 수정"
            recruitmentForm.fill(recruitmentService.getDataById(parameter))
            submitButton.text = "수정"
        }
    }

    private fun createSubmitButton(): Button {
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

    private fun createButtons(): Component {
        return HorizontalLayout(submitButton, cancelButton).apply {
            setSizeFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        }
    }
}
