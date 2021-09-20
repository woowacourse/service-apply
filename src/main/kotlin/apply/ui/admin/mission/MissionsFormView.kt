package apply.ui.admin.mission

import apply.application.EvaluationService
import apply.application.MissionService
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
import support.views.createNotification
import support.views.createPrimaryButton
import support.views.toDisplayName

@Route(value = "admin/mission/selections", layout = BaseLayout::class)
class MissionsFormView(
    private val evaluationService: EvaluationService,
    private val missionService: MissionService
) : VerticalLayout(), HasUrlParameter<String> {
    private var recruitmentId: Long = 0L
    private val title: Title = Title()
    private val missionForm: MissionForm by lazy {
        MissionForm(
            evaluationService.getAllSelectDataByRecruitmentId(recruitmentId)
        )
    }
    private val submitButton: Button = createSubmitButton()

    override fun setParameter(event: BeforeEvent, @WildcardParameter parameter: String) {
        val result = FORM_URL_PATTERN.find(parameter)
        result?.let {
            val (id, value) = it.destructured
            recruitmentId = id.toLong()
            setDisplayName(value.toDisplayName())
            if (value == EDIT_VALUE) {
                // TODO: 수정 기능 구현
            }
        } ?: UI.getCurrent().page.history.back()
        add(title, missionForm, createButtons())
    }

    private fun setDisplayName(displayName: String) {
        title.text = "과제 $displayName"
        submitButton.text = displayName
    }

    private fun createSubmitButton(): Button {
        return createPrimaryButton {
            missionForm.bindOrNull()?.let {
                try {
                    missionService.save(it)
                } catch (e: IllegalArgumentException) {
                    createNotification(e.localizedMessage).open()
                }
                UI.getCurrent().navigate(MissionSelectionView::class.java, recruitmentId)
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
            UI.getCurrent().navigate(MissionSelectionView::class.java, recruitmentId)
        }
    }
}
