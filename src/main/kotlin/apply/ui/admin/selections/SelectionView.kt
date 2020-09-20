package apply.ui.admin.selections

import apply.application.ApplicantService
import apply.application.ApplicationService
import apply.application.RecruitmentItemService
import apply.application.RecruitmentService
import apply.domain.applicant.Applicant
import apply.domain.application.Application
import apply.ui.admin.BaseLayout
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.html.H4
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.data.renderer.Renderer
import com.vaadin.flow.router.BeforeEvent
import com.vaadin.flow.router.HasUrlParameter
import com.vaadin.flow.router.Route
import com.vaadin.flow.router.WildcardParameter
import support.views.addSortableColumn
import support.views.addSortableDateColumn
import support.views.createNormalButton
import support.views.createPrimarySmallButton
import support.views.createSearchBar
import support.views.createSuccessButton

@Route(value = "admin/selections", layout = BaseLayout::class)
class SelectionView(
    private val applicantService: ApplicantService,
    private val applicationService: ApplicationService,
    private val recruitmentService: RecruitmentService,
    private val recruitmentItemService: RecruitmentItemService
) : VerticalLayout(), HasUrlParameter<Long> {
    private var recruitmentId: Long = 0L

    private fun createTitle(): Component {
        return HorizontalLayout(H1(recruitmentService.getById(recruitmentId).title)).apply {
            setWidthFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        }
    }

    private fun createMenu(): Component {
        return HorizontalLayout(
            createSearchBar {
                removeAll()
                add(createTitle(), createMenu(), createGrid(applicantService.findByValue(it)))
            },
            createSuccessButton("다운로드") {
                // Todo: 엑셀 다운로드
            }
        ).apply {
            setWidthFull()
            isSpacing = false
            justifyContentMode = FlexComponent.JustifyContentMode.BETWEEN
        }
    }

    private fun createGrid(applicants: List<Applicant>): Component {
        return Grid<Applicant>(10).apply {
            addSortableColumn("이름", Applicant::name)
            addSortableColumn("이메일", Applicant::email)
            addSortableColumn("전화번호", Applicant::phoneNumber)
            addSortableColumn("성별") { it.gender.title }
            addSortableDateColumn("생년월일", Applicant::birthday)
            addColumn(createButtonRenderer()).apply { isAutoWidth = true }
            setItems(applicants)
        }
    }

    private fun createButtonRenderer(): Renderer<Applicant> {
        return ComponentRenderer<Component, Applicant> { applicant ->
            createPrimarySmallButton("지원서") {
                val dialog = Dialog()
                dialog.add(
                    *createRecruitmentItems(
                        applicationService.getByRecruitmentIdAndApplicantId(
                            recruitmentId,
                            applicant.id
                        )
                    )
                )
                dialog.width = "800px"
                dialog.height = "90%"
                dialog.open()
            }
        }
    }

    private fun createRecruitmentItems(application: Application): Array<Component> {
        val answers = application.answers
            .items
            .map { it.recruitmentItemId to it.contents }
            .toMap()
        val items = recruitmentItemService.findByRecruitmentIdOrderByPosition(recruitmentId)
            .map {
                createItem(it.title, createAnswer(answers.getValue(it.id)))
            }.toTypedArray()
        return addIfExist(items, application.referenceUrl)
    }

    private fun addIfExist(items: Array<Component>, referenceUrl: String): Array<Component> {
        return when {
            referenceUrl.isNotEmpty() -> {
                val referenceItem = createItem(
                    "포트폴리오",
                    createNormalButton(referenceUrl) {
                        UI.getCurrent().page.open(referenceUrl, "_blank")
                    }
                )
                items.plusElement(referenceItem)
            }
            else -> items
        }
    }

    private fun createItem(title: String, component: Component): Component {
        return Div(H4(title), component).apply {
            setWidthFull()
            justifyContentMode = FlexComponent.JustifyContentMode.START
        }
    }

    private fun createAnswer(answer: String): Component {
        return TextArea().apply {
            setWidthFull()
            isReadOnly = true
            value = answer
            justifyContentMode = FlexComponent.JustifyContentMode.START
        }
    }

    override fun setParameter(event: BeforeEvent, @WildcardParameter parameter: Long) {
        this.recruitmentId = parameter
        val applicantIds = applicationService.findAllByRecruitmentId(recruitmentId)
            .map { it.applicantId }
        add(createTitle(), createMenu(), createGrid(applicantService.findAllByIds(applicantIds)))
    }
}
