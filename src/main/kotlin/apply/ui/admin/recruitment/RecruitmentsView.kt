package apply.ui.admin.recruitment

import apply.application.RecruitmentResponse
import apply.application.RecruitmentService
import apply.domain.recruitment.RecruitmentStatus
import apply.ui.admin.BaseLayout
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.data.renderer.Renderer
import com.vaadin.flow.router.Route
import support.views.EDIT_VALUE
import support.views.NEW_VALUE
import support.views.addSortableColumn
import support.views.addSortableDateTimeColumn
import support.views.createDeleteButtonWithDialog
import support.views.createPrimaryButton
import support.views.createPrimarySmallButton

@Route(value = "admin/recruitments", layout = BaseLayout::class)
class RecruitmentsView(private val recruitmentService: RecruitmentService) : VerticalLayout() {
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
                UI.getCurrent().navigate(RecruitmentsFormView::class.java, NEW_VALUE)
            }
        ).apply {
            setSizeFull()
            justifyContentMode = FlexComponent.JustifyContentMode.END
        }
    }

    private fun createGrid(): Component {
        return Grid<RecruitmentResponse>(10).apply {
            addSortableColumn("모집명", RecruitmentResponse::title)
            addSortableColumn("기수") { it.term.name }
            addSortableColumn("상태") { it.status.toText() }
            addSortableColumn("공개 여부") { it.hidden.toText() }
            addSortableDateTimeColumn("시작일시", RecruitmentResponse::startDateTime)
            addSortableDateTimeColumn("종료일시", RecruitmentResponse::endDateTime)
            addColumn(createButtonRenderer()).apply { isAutoWidth = true }
            setItems(recruitmentService.findAll())
        }
    }

    private fun createButtonRenderer(): Renderer<RecruitmentResponse> {
        return ComponentRenderer<Component, RecruitmentResponse> { it -> createButtons(it) }
    }

    private fun createButtons(recruitment: RecruitmentResponse): Component {
        return HorizontalLayout(
            createEditButton(recruitment),
            createDeleteButton(recruitment).apply { isEnabled = !recruitment.recruitable }
        )
    }

    private fun createEditButton(recruitment: RecruitmentResponse): Component {
        return createPrimarySmallButton("수정") {
            UI.getCurrent().navigate(RecruitmentsFormView::class.java, "${recruitment.id}/$EDIT_VALUE")
        }
    }

    private fun createDeleteButton(recruitment: RecruitmentResponse): Button {
        return createDeleteButtonWithDialog("모집을 삭제하시겠습니까?") {
            recruitmentService.deleteById(recruitment.id)
        }
    }

    private fun RecruitmentStatus.toText(): String {
        return when (this) {
            RecruitmentStatus.RECRUITABLE -> "모집 예정"
            RecruitmentStatus.RECRUITING -> "모집 중"
            RecruitmentStatus.UNRECRUITABLE -> "모집 중지"
            RecruitmentStatus.ENDED -> "모집 종료"
        }
    }

    private fun Boolean.toText(): String {
        return if (this) {
            "비공개"
        } else {
            "공개"
        }
    }
}
