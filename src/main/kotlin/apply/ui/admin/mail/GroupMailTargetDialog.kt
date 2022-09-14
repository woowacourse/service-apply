package apply.ui.admin.mail

import apply.application.EvaluationSelectData
import apply.application.EvaluationService
import apply.application.MailTargetResponse
import apply.application.MailTargetService
import apply.application.RecruitmentResponse
import apply.application.RecruitmentService
import apply.domain.evaluationtarget.EvaluationStatus
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.data.provider.ListDataProvider
import dev.mett.vaadin.tooltip.Tooltips
import dev.mett.vaadin.tooltip.config.TooltipConfiguration
import support.views.NO_NAME
import support.views.addSortableColumn
import support.views.createContrastButton
import support.views.createItemSelect
import support.views.createPrimaryButton
import support.views.toText

class GroupMailTargetDialog(
    private val recruitmentService: RecruitmentService,
    private val evaluationService: EvaluationService,
    private val mailTargetService: MailTargetService,
    private val accept: (Collection<MailTargetResponse>) -> Unit
) : Dialog() {
    private val mailTargetsGrid: Grid<MailTargetResponse> = createMailTargetsGrid()

    init {
        add(createHeader(), createSearchFilter(), mailTargetsGrid, createButtons())
        width = "900px"
        open()
    }

    private fun createHeader(): VerticalLayout {
        return VerticalLayout(H2("그룹 불러오기")).apply {
            isPadding = false
            element.style.set("margin-bottom", "10px")
        }
    }

    private fun createMailTargetsGrid(): Grid<MailTargetResponse> {
        return Grid<MailTargetResponse>(10).apply {
            addSortableColumn("이름") { it.name ?: NO_NAME }
            addSortableColumn("이메일", MailTargetResponse::email)
        }
    }

    private fun createSearchFilter(): HorizontalLayout {
        val evaluationItem = createItemSelect<EvaluationSelectData>("평가")
        return HorizontalLayout(
            createRecruitmentItem(evaluationItem), evaluationItem, createEvaluationStatusItem(evaluationItem),
        ).apply {
            element.style.set("margin-bottom", "10px")
        }
    }

    private fun createRecruitmentItem(evaluationItem: Select<EvaluationSelectData>): Select<RecruitmentResponse> {
        return createItemSelect<RecruitmentResponse>("모집").apply {
            setItems(*recruitmentService.findAll().toTypedArray())
            setItemLabelGenerator { it.title }
            addValueChangeListener {
                evaluationItem.apply {
                    setItems(*evaluationService.getAllSelectDataByRecruitmentId(it.value.id).toTypedArray())
                    setItemLabelGenerator { it.title }
                }
            }
        }
    }

    private fun createEvaluationStatusItem(
        evaluationItem: Select<EvaluationSelectData>
    ): Select<EvaluationStatus> {
        return createItemSelect<EvaluationStatus>("평가 상태").apply {
            val descriptionTooltip = TooltipConfiguration("평가되지 않은 탈락자는 나오지 않습니다.").apply { trigger = "mouseenter" }
            Tooltips.getCurrent().setTooltip(this, descriptionTooltip)
            setItems(*EvaluationStatus.values())
            setItemLabelGenerator { it.toText() }
            addValueChangeListener {
                mailTargetsGrid.setItems(mailTargetService.findMailTargets(evaluationItem.value.id, it.value))
            }
        }
    }

    private fun createButtons(): Component {
        return HorizontalLayout(createAddButton(), createCancelButton()).apply {
            setWidthFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
            element.style.set("margin-top", "20px")
        }
    }

    private fun createAddButton(): Button {
        return createPrimaryButton("추가") {
            val dataProvider = mailTargetsGrid.dataProvider as ListDataProvider
            accept(dataProvider.items)
            close()
        }
    }

    private fun createCancelButton(): Button {
        return createContrastButton("닫기") {
            close()
        }
    }
}
