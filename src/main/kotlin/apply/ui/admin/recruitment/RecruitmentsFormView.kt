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

@Route(value = "admin/recruitments", layout = BaseLayout::class)
class RecruitmentsFormView(
    private val recruitmentService: RecruitmentService
) : VerticalLayout(), HasUrlParameter<String> {
    private val title: Title = Title("모집 생성")
    private val recruitmentForm: RecruitmentForm = RecruitmentForm()
    private val submitButton: Button = createSubmitButton()
    private val cancelButton: Button = createCancelButton()

    init {
        add(title, recruitmentForm, createButtons())
    }

    override fun setParameter(event: BeforeEvent, @WildcardParameter parameter: String) {
        val result = FORM_URL_PATTERN.find(parameter)
        result?.let {
            val (id, value) = it.destructured
            if (value == EDIT_VALUE) {
                title.text = "모집 수정"
                recruitmentForm.fill(recruitmentService.getNotEndedDataById(id.toLong()))
                submitButton.text = "수정"
            }
        } ?: UI.getCurrent().page.history.back() // TODO: 에러 화면을 구현한다.
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
