package apply.ui.admin.recruitment

import apply.application.RecruitmentService
import apply.domain.recruitment.Recruitment
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
                UI.getCurrent().navigate(RecruitmentsFormView::class.java, "new")
            }
        ).apply {
            setSizeFull()
            justifyContentMode = FlexComponent.JustifyContentMode.END
        }
    }

    private fun createGrid(): Component {
        return Grid<Recruitment>(10).apply {
            addSortableColumn("모집명", Recruitment::title)
            addSortableColumn("상태") { it.status.toText() }
            addSortableColumn("공개 여부") { it.isHidden.toText() }
            addSortableDateTimeColumn("시작일시", Recruitment::startDateTime)
            addSortableDateTimeColumn("종료일시", Recruitment::endDateTime)
            addColumn(createButtonRenderer()).apply { isAutoWidth = true }
            setItems(recruitmentService.findAll())
        }
    }

    private fun createButtonRenderer(): Renderer<Recruitment> {
        return ComponentRenderer<Component, Recruitment> { it ->
            if (it.status == RecruitmentStatus.ENDED) {
                HorizontalLayout()
            } else {
                createButtons(it)
            }
        }
    }

    private fun createButtons(recruitment: Recruitment): Component {
        return HorizontalLayout(
            createEditButton(recruitment),
            createDeleteButton(recruitment).apply { isEnabled = !recruitment.canRecruit }
        )
    }

    private fun createEditButton(recruitment: Recruitment): Component {
        return createPrimarySmallButton("수정") {
            UI.getCurrent().navigate(RecruitmentsFormView::class.java, "${recruitment.id}/edit")
        }
    }

    private fun createDeleteButton(recruitment: Recruitment): Button {
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
