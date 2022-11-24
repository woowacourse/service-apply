package apply.ui.admin.evaluation

import apply.application.EvaluationGridResponse
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
import support.views.EDIT_VALUE
import support.views.NEW_VALUE
import support.views.addSortableColumn
import support.views.createDeleteButtonWithDialog
import support.views.createPrimaryButton
import support.views.createPrimarySmallButton

@Route(value = "admin/evaluations", layout = BaseLayout::class)
class EvaluationsView(private val evaluationService: EvaluationService) : VerticalLayout() {
    init {
        add(createTitle(), createButton(), createGrid())
        setSizeFull()
    }

    private fun createTitle(): Component {
        return HorizontalLayout(H1("평가 관리")).apply {
            setWidthFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        }
    }

    private fun createButton(): Component {
        return HorizontalLayout(
            createPrimaryButton("생성") {
                UI.getCurrent().navigate(EvaluationsFormView::class.java, NEW_VALUE)
            }
        ).apply {
            setWidthFull()
            justifyContentMode = FlexComponent.JustifyContentMode.END
        }
    }

    private fun createGrid(): Component {
        return Grid<EvaluationGridResponse>(10).apply {
            addSortableColumn("평가명", EvaluationGridResponse::title)
            addSortableColumn("모집명", EvaluationGridResponse::recruitmentTitle)
            addSortableColumn("이전 평가명", EvaluationGridResponse::beforeEvaluationTitle)
            addColumn(createEditAndDeleteButton()).apply { isAutoWidth = true }
            setItems(evaluationService.findAllWithRecruitment())
        }
    }

    private fun createEditAndDeleteButton(): Renderer<EvaluationGridResponse> {
        return ComponentRenderer { evaluationResponse ->
            HorizontalLayout(
                createPrimarySmallButton("수정") {
                    UI.getCurrent().navigate(EvaluationsFormView::class.java, "${evaluationResponse.id}/$EDIT_VALUE")
                },
                createDeleteButtonWithDialog("평가를 삭제하시겠습니까?") {
                    evaluationService.deleteById(evaluationResponse.id)
                }
            )
        }
    }
}
