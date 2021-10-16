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
import com.vaadin.flow.router.Route
import com.vaadin.flow.router.WildcardParameter
import support.views.EDIT_VALUE
import support.views.FORM_URL_PATTERN
import support.views.Title
import support.views.createContrastButton
import support.views.createPrimaryButton
import support.views.toDisplayName

@Route(value = "admin/recruitments", layout = BaseLayout::class)
class RecruitmentsFormView(
    private val recruitmentService: RecruitmentService
) : VerticalLayout(), HasUrlParameter<String> {
    private val title: Title = Title()
    private val recruitmentForm: RecruitmentForm = RecruitmentForm(recruitmentService.findAllTermSelectData())
    private val submitButton: Button = createSubmitButton()

    init {
        add(title, recruitmentForm, createButtons())
    }

    override fun setParameter(event: BeforeEvent, @WildcardParameter parameter: String) {
        val result = FORM_URL_PATTERN.find(parameter) ?: return UI.getCurrent().page.history.back()
        val (id, value) = result.destructured
        setDisplayName(value.toDisplayName())
        if (value == EDIT_VALUE) {
            recruitmentForm.fill(recruitmentService.getNotEndedDataById(id.toLong()))
            submitButton.isVisible = false
        }
    }

    private fun setDisplayName(displayName: String) {
        title.text = "모집 $displayName"
        submitButton.text = displayName
    }

    private fun createSubmitButton(): Button {
        return createPrimaryButton {
            recruitmentForm.bindOrNull()?.let {
                recruitmentService.save(it)
                UI.getCurrent().page.setLocation("admin/recruitments")
            }
        }
    }

    private fun createButtons(): Component {
        return HorizontalLayout(submitButton, createCancelButton()).apply {
            setSizeFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        }
    }

    private fun createCancelButton(): Button {
        return createContrastButton("취소") {
            UI.getCurrent().navigate(RecruitmentsView::class.java)
        }
    }
}
