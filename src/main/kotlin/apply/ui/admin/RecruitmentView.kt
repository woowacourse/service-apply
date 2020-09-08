package apply.ui.admin

import apply.application.RecruitmentService
import apply.domain.recruitment.Recruitment
import apply.domain.recruitment.RecruitmentStatus
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.data.renderer.Renderer
import com.vaadin.flow.router.Route
import support.addSortableColumn
import support.addSortableDateTimeColumn
import support.createContrastButtonWithDialog
import support.createDeleteButtonWithDialog
import support.createPrimaryButton
import support.createPrimarySmallButton
import support.createSuccessButtonWithDialog

@Route(value = "admin/recruitment", layout = BaseLayout::class)
class RecruitmentView(private val recruitmentService: RecruitmentService) : VerticalLayout() {
    init {
        add(createTitle(), createButton(), createGrid())
    }

    private fun createTitle(): Component {
        return HorizontalLayout(H1("모집 관리")).apply {
            setSizeFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        }
    }

    private fun createButton(): Component {
        return HorizontalLayout(
            createPrimaryButton("생성") {
                // TODO: 모집 관리 페이지에 생성 뷰를 구현한다.
            }
        ).apply {
            setSizeFull()
            justifyContentMode = FlexComponent.JustifyContentMode.END
        }
    }

    private fun createGrid(): Component {
        return Grid<Recruitment>(10).apply {
            addSortableColumn("모집명", Recruitment::title)
            addSortableColumn("상태") { it.status.title }
            addSortableDateTimeColumn("시작일시", Recruitment::startDateTime)
            addSortableDateTimeColumn("종료일시", Recruitment::endDateTime)
            addColumn(createButtonRenderer()).apply { isAutoWidth = true }
            setItems(recruitmentService.findAll())
        }
    }

    private fun createButtonRenderer(): Renderer<Recruitment> {
        return ComponentRenderer<Component, Recruitment> { recruitment ->
            when (recruitment.status) {
                RecruitmentStatus.RECRUITABLE -> createButtonsWhenRecruitable(recruitment)
                RecruitmentStatus.UNRECRUITABLE -> createButtonsWhenUnrecruitable(recruitment)
                RecruitmentStatus.ENDED -> HorizontalLayout()
            }
        }
    }

    private fun createButtonsWhenRecruitable(recruitment: Recruitment): Component {
        return HorizontalLayout(
            createContrastButtonWithDialog(
                "모집 중지",
                "모집을 중지하면 시작일시가 지나도 지원이 불가능합니다. 모집을 중단하시겠습니까?"
            ) {
                recruitmentService.stop(recruitment.id)
            },
            createEditButton()
        )
    }

    private fun createButtonsWhenUnrecruitable(recruitment: Recruitment): HorizontalLayout {
        return HorizontalLayout(
            createSuccessButtonWithDialog(
                "모집 시작",
                "모집 시작일시가 지나면 자동으로 지원이 가능합니다. 모집을 시작하시겠습니까?"
            ) {
                recruitmentService.start(recruitment.id)
            },
            createEditButton(),
            createDeleteButtonWithDialog("모집을 삭제하시겠습니까?") {
                recruitmentService.deleteById(recruitment.id)
            }
        )
    }

    private fun createEditButton(): Component {
        return createPrimarySmallButton("수정") {
            // TODO: 모집 관리 페이지에 수정 뷰를 구현한다.
        }
    }
}
