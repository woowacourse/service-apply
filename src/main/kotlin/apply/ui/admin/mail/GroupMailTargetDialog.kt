package apply.ui.admin.mail

import apply.application.EvaluationService
import apply.application.MailTargetResponse
import apply.application.MailTargetService
import apply.application.RecruitmentResponse
import apply.application.RecruitmentService
import apply.domain.evaluation.Evaluation
import apply.domain.evaluationtarget.EvaluationStatus
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.data.provider.ListDataProvider
import support.views.NO_NAME
import support.views.addSortableColumn
import support.views.createContrastButton
import support.views.createItemSelect
import support.views.createNotification
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
        add(H2("그룹 불러오기"), createSearchFilter(), mailTargetsGrid, createButtons())
        width = "900px"
        height = "70%"
        open()
    }

    private fun createMailTargetsGrid(): Grid<MailTargetResponse> {
        return Grid<MailTargetResponse>(10).apply {
            addSortableColumn("이름") { it.name ?: NO_NAME }
            addSortableColumn("이메일", MailTargetResponse::email)
        }
    }

    private fun createSearchFilter(): HorizontalLayout {
        val evaluationItem = createItemSelect<Evaluation>("평가")
        return HorizontalLayout(
            createRecruitmentItem(evaluationItem), evaluationItem, createEvaluationStatusItem(evaluationItem),
        ).apply {
            element.style.set("margin-bottom", "10px")
        }
    }

    private fun createRecruitmentItem(evaluationItem: Select<Evaluation>): Select<RecruitmentResponse> {
        return createItemSelect<RecruitmentResponse>("모집").apply {
            setItems(*recruitmentService.findAll().toTypedArray())
            setItemLabelGenerator { it.title }
            addValueChangeListener {
                evaluationItem.apply {
                    setItems(*evaluationService.findAllByRecruitmentId(it.value.id).toTypedArray())
                    setItemLabelGenerator { it.title }
                }
            }
        }
    }

    private fun createEvaluationStatusItem(
        evaluationItem: Select<Evaluation>
    ): Select<EvaluationStatus> {
        return createItemSelect<EvaluationStatus>("평가 상태").apply {
            setItems(*EvaluationStatus.values())
            setItemLabelGenerator { it.toText() }
            addValueChangeListener {
                mailTargetsGrid.setItems(mailTargetService.findMailTargets(evaluationItem.value.id, it.value))
                if (it.value == EvaluationStatus.FAIL) {
                    createNotification("점수가 0인 탈락자는 나오지 않습니다.", 3000)
                }
            }
        }
    }

    private fun createButtons(): Component {
        return HorizontalLayout(createAddButton(), createCancelButton()).apply {
            setSizeFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
            element.style.set("margin-top", "10px")
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
        return createContrastButton("취소") {
            close()
        }
    }
}
