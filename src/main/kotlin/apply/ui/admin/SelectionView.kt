package apply.ui.admin

import apply.application.ApplicantService
import apply.application.ApplicationService
import apply.application.RecruitmentItemService
import apply.application.RecruitmentService
import apply.domain.applicant.Applicant
import apply.domain.application.Application
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.H1
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
import support.addSortableColumn
import support.addSortableDateColumn
import support.createPrimarySmallButton
import support.createSearchBar
import support.createSuccessButton

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
        return recruitmentItemService.findByRecruitmentId(recruitmentId)
            .sortedBy { it.position }
            .map {
                createQnAs(it.title, answers[it.id] ?: throw IllegalArgumentException())
            }.toTypedArray()
    }

    private fun createQnAs(title: String, contents: String): Component {
        return createAnswer(title, contents).apply {
            setWidthFull()
            justifyContentMode = FlexComponent.JustifyContentMode.START
        }
    }

    private fun createAnswer(title: String, answer: String): Component {
        return TextArea(title).apply {
            setWidthFull()
            isReadOnly = true
            value = answer
            justifyContentMode = FlexComponent.JustifyContentMode.START
        }
    }

    override fun setParameter(event: BeforeEvent?, @WildcardParameter parameter: Long) {
        this.recruitmentId = parameter
        // Todo: RecruitmentId 별로 지원자를 불러오도록 수정
        add(createTitle(), createMenu(), createGrid(applicantService.findAll()))
    }
}
