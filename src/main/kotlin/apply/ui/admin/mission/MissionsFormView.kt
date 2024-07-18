package apply.ui.admin.mission

import apply.application.EvaluationService
import apply.application.MissionData
import apply.application.MissionService
import apply.ui.admin.BaseLayout
import apply.ui.admin.PreviewDialog
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
import support.views.NEW_VALUE
import support.views.Title
import support.views.createContrastButton
import support.views.createNotification
import support.views.createPrimaryButton
import support.views.toDisplayName

private val MISSION_FORM_URL_PATTERN: Regex = Regex("^(\\d*)/?(\\d*)/?($NEW_VALUE|$EDIT_VALUE)$")
private const val DATA_NOT_BIND_MESSAGE: String = "모든 항목이 잘 입력되었는지 확인해 주세요."

@Route(value = "admin/missions", layout = BaseLayout::class)
class MissionsFormView(
    private val evaluationService: EvaluationService,
    private val missionService: MissionService
) : VerticalLayout(), HasUrlParameter<String> {
    private var recruitmentId: Long = 0L
    private val title: Title = Title()
    private val missionForm: MissionForm by lazy {
        MissionForm(evaluationService.getAllSelectDataByRecruitmentId(recruitmentId)) {
            missionService.findEvaluationItems(it)
        }
    }
    private val submitButton: Button = createSubmitButton()
    private val buttons: Component = createButtons()

    override fun setParameter(event: BeforeEvent, @WildcardParameter parameter: String) {
        val result = MISSION_FORM_URL_PATTERN.find(parameter) ?: return UI.getCurrent().page.history.back()
        val (recruitmentId, missionId, value) = result.destructured
        setDisplayName(value.toDisplayName())
        this.recruitmentId = recruitmentId.toLong()
        if (value == EDIT_VALUE) {
            missionForm.fill(missionService.getDataById(missionId.toLong()))
        }
        add(title, missionForm, buttons)
    }

    private fun setDisplayName(displayName: String) {
        title.text = "과제 $displayName"
        submitButton.text = displayName
    }

    private fun createButtons(): Component {
        return HorizontalLayout(submitButton, createCancelButton(), createPreviewButton()).apply {
            setSizeFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        }
    }

    private fun createSubmitButton(): Button {
        return createPrimaryButton {
            handleMissionData { mission ->
                try {
                    missionService.save(mission)
                    UI.getCurrent().navigate(MissionsView::class.java, recruitmentId)
                } catch (e: Exception) {
                    createNotification(e.localizedMessage)
                }
            }
        }
    }

    private fun createCancelButton(): Button {
        return createContrastButton("취소") {
            UI.getCurrent().navigate(MissionsView::class.java, recruitmentId)
        }
    }

    private fun createPreviewButton(): Button {
        return createContrastButton("미리보기") {
            handleMissionData { mission ->
                val formattedDescription = missionService.parseDescription(mission.description)
                PreviewDialog("<div>$formattedDescription</div>")
            }
        }
    }

    private fun handleMissionData(action: (MissionData) -> Unit) {
        val result = missionForm.bindOrNull()
        if (result == null) {
            createNotification(DATA_NOT_BIND_MESSAGE)
        } else {
            action(result)
        }
    }
}
