package apply.ui.admin.recruitment

import apply.application.RecruitmentService
import apply.domain.recruitment.Recruitment
import apply.domain.recruitment.RecruitmentStatus
import apply.ui.admin.BaseLayout
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.data.provider.DataProvider
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.data.renderer.Renderer
import com.vaadin.flow.router.Route
import org.vaadin.klaudeta.PaginatedGrid
import support.views.EDIT_VALUE
import support.views.NEW_VALUE
import support.views.Title
import support.views.addBackEndSortableColumn
import support.views.addBackEndSortableDateTimeColumn
import support.views.addNormalColumn
import support.views.createDeleteButtonWithDialog
import support.views.createPrimaryButton
import support.views.createPrimarySmallButton
import support.views.toMap

@Route(value = "admin/recruitments", layout = BaseLayout::class)
class RecruitmentsView(private val recruitmentService: RecruitmentService) : VerticalLayout() {
    init {
        add(Title("모집 관리"), createButton(), createGrid())
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
        return PaginatedGrid<Recruitment>().apply {
            addBackEndSortableColumn("모집명", "title", Recruitment::title)
            addNormalColumn("상태") { it.status.toText() }
            addBackEndSortableColumn("공개 여부", "isHidden") { it.hidden.toText() }
            addBackEndSortableDateTimeColumn("시작일시", "period.startDateTime", Recruitment::startDateTime)
            addBackEndSortableDateTimeColumn("종료일시", "period.endDateTime", Recruitment::endDateTime)
            addNormalColumn(createButtonRenderer())
            pageSize = 10
            isMultiSort = true
            dataProvider = DataProvider.fromCallbacks(
                { query -> recruitmentService.findAll(query.offset, query.limit, query.sortOrders.toMap()).stream() },
                { recruitmentService.count().toInt() }
            )
        }
    }

    private fun createButtonRenderer(): Renderer<Recruitment> {
        return ComponentRenderer<Component, Recruitment> { it -> createButtons(it) }
    }

    private fun createButtons(recruitment: Recruitment): Component {
        return HorizontalLayout(
            createEditButton(recruitment),
            createDeleteButton(recruitment).apply { isEnabled = !recruitment.recruitable }
        )
    }

    private fun createEditButton(recruitment: Recruitment): Component {
        return createPrimarySmallButton("수정") {
            UI.getCurrent().navigate(RecruitmentsFormView::class.java, "${recruitment.id}/$EDIT_VALUE")
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
