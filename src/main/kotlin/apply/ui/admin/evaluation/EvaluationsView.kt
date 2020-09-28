package apply.ui.admin.evaluation

import apply.application.EvaluationResponse
import apply.application.EvaluationService
import apply.ui.admin.BaseLayout
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.data.renderer.Renderer
import com.vaadin.flow.router.Route
import support.views.addSortableColumn
import support.views.createDeleteButtonWithDialog
import support.views.createPrimaryButton
import support.views.createPrimarySmallButton

@Route(value = "admin/evaluations", layout = BaseLayout::class)
class EvaluationsView(private val evaluationService: EvaluationService) : VerticalLayout() {
    init {
        add(createTitle(), createButton(), createGrid())
    }

    private fun createTitle(): Component {
        return HorizontalLayout(H1("평가 관리")).apply {
            setSizeFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        }
    }

    private fun createButton(): Component {
        return HorizontalLayout(
            createPrimaryButton("생성") {
                UI.getCurrent().navigate(EvaluationsFormView::class.java)
            }
        ).apply {
            setSizeFull()
            justifyContentMode = FlexComponent.JustifyContentMode.END
        }
    }

    private fun createGrid(): Component {
        return Grid<EvaluationResponse>(10).apply {
            addSortableColumn("평가명", EvaluationResponse::title)
            addSortableColumn("모집명", EvaluationResponse::recruitmentTitle)
            addSortableColumn("이전 평가명", EvaluationResponse::beforeEvaluationTitle)
            addColumn(createEditAndDeleteButton()).apply { isAutoWidth = true }
            setItems(evaluationService.findAllWithRecruitment())
        }
    }

    private fun createEditAndDeleteButton(): Renderer<EvaluationResponse> {
        return ComponentRenderer<Component, EvaluationResponse> { evaluationResponse ->
            HorizontalLayout(
                createPrimarySmallButton("수정") {
                    // TODO: 모집 관리 페이지에 수정 뷰를 구현한다.
                },
                createDeleteButtonWithDialog("평가를 삭제하시겠습니까?") {
                    evaluationService.deleteById(evaluationResponse.id)
                }
            )
        }
    }
}
