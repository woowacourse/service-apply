package apply.ui.admin.mission

import apply.application.MissionAndEvaluationResponse
import apply.application.MissionService
import apply.application.RecruitmentService
import apply.domain.mission.MissionStatus
import apply.ui.admin.BaseLayout
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.data.renderer.Renderer
import com.vaadin.flow.router.BeforeEvent
import com.vaadin.flow.router.HasUrlParameter
import com.vaadin.flow.router.Route
import com.vaadin.flow.router.WildcardParameter
import support.views.EDIT_VALUE
import support.views.NEW_VALUE
import support.views.Title
import support.views.addSortableColumn
import support.views.addSortableDateTimeColumn
import support.views.createDeleteButtonWithDialog
import support.views.createPrimaryButton
import support.views.createPrimarySmallButton

@Route(value = "admin/missions", layout = BaseLayout::class)
class MissionsView(
    private val recruitmentService: RecruitmentService,
    private val missionService: MissionService
) : VerticalLayout(), HasUrlParameter<Long> {
    private var recruitmentId: Long = 0L

    override fun setParameter(event: BeforeEvent, @WildcardParameter parameter: Long) {
        recruitmentId = parameter
        add(createTitle(), createButton(), createGrid())
    }

    private fun createTitle(): Component {
        return Title("${recruitmentService.getById(recruitmentId).title} 과제 관리")
    }

    private fun createButton(): Component {
        return HorizontalLayout(
            createPrimaryButton("생성") {
                UI.getCurrent().navigate(MissionsFormView::class.java, "$recruitmentId/$NEW_VALUE")
            }
        ).apply {
            setSizeFull()
            justifyContentMode = FlexComponent.JustifyContentMode.END
        }
    }

    private fun createGrid(): Component {
        return Grid<MissionAndEvaluationResponse>(10).apply {
            addSortableColumn("과제명", MissionAndEvaluationResponse::title)
            addSortableColumn("평가명", MissionAndEvaluationResponse::evaluationTitle)
            addSortableDateTimeColumn("시작일시", MissionAndEvaluationResponse::startDateTime)
            addSortableDateTimeColumn("종료일시", MissionAndEvaluationResponse::endDateTime)
            addSortableColumn("상태") { it.status.toText() }
            addSortableColumn("공개 여부") { it.hidden.toText() }
            addSortableColumn("제출 방식") { it.submissionMethod.label }
            addColumn(createButtonRenderer()).apply { isAutoWidth = true }
            setItems(missionService.findAllByRecruitmentId(recruitmentId))
        }
    }

    private fun createButtonRenderer(): Renderer<MissionAndEvaluationResponse> {
        return ComponentRenderer { it -> createButtons(it) }
    }

    private fun createButtons(mission: MissionAndEvaluationResponse): Component {
        return HorizontalLayout(
            createEditButton(mission),
            createDeleteButton(mission).apply { isEnabled = !mission.submittable }
        )
    }

    private fun createEditButton(mission: MissionAndEvaluationResponse): Component {
        return createPrimarySmallButton("수정") {
            UI.getCurrent().navigate(MissionsFormView::class.java, "$recruitmentId/${mission.id}/$EDIT_VALUE")
        }
    }

    private fun createDeleteButton(mission: MissionAndEvaluationResponse): Button {
        return createDeleteButtonWithDialog("과제를 삭제하시겠습니까?") {
            missionService.deleteById(mission.id)
        }
    }

    private fun MissionStatus.toText(): String {
        return when (this) {
            MissionStatus.SUBMITTABLE -> "제출 예정"
            MissionStatus.SUBMITTING -> "제출 중"
            MissionStatus.UNSUBMITTABLE -> "제출 중지"
            MissionStatus.ENDED -> "제출 종료"
        }
    }

    private fun Boolean.toText(): String {
        return if (this) "비공개" else "공개"
    }
}
