package apply.ui.admin.evaluation

import apply.application.EvaluationResponse
import apply.application.EvaluationService
import apply.ui.admin.BaseLayout
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.html.H1
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
import support.views.addBackEndSortableColumn
import support.views.createDeleteButtonWithDialog
import support.views.createPrimaryButton
import support.views.createPrimarySmallButton
import support.views.toMap

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
                UI.getCurrent().navigate(EvaluationsFormView::class.java, NEW_VALUE)
            }
        ).apply {
            setSizeFull()
            justifyContentMode = FlexComponent.JustifyContentMode.END
        }
    }

    private fun createGrid(): Component {
        return PaginatedGrid<EvaluationResponse>().apply {
            addBackEndSortableColumn("평가명", "title", EvaluationResponse::title)
            addBackEndSortableColumn("모집명", "recruitmentId", EvaluationResponse::recruitmentTitle)
            addBackEndSortableColumn("이전 평가명", "beforeEvaluationId", EvaluationResponse::beforeEvaluationTitle)
            addColumn(createEditAndDeleteButton()).apply { isAutoWidth = true }
            pageSize = 10
            isMultiSort = true
            dataProvider = DataProvider.fromCallbacks(
                { query -> evaluationService.findAll(query.offset, query.limit, query.sortOrders.toMap()).stream() },
                { evaluationService.count().toInt() }
            )
        }
    }

    private fun createEditAndDeleteButton(): Renderer<EvaluationResponse> {
        return ComponentRenderer<Component, EvaluationResponse> { evaluationResponse ->
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
