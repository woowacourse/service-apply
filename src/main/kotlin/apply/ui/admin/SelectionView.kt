package apply.ui.admin

import apply.application.ApplicantService
import apply.application.RecruitmentService
import apply.domain.applicant.Applicant
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.data.renderer.Renderer
import com.vaadin.flow.router.BeforeEvent
import com.vaadin.flow.router.HasUrlParameter
import com.vaadin.flow.router.Route
import com.vaadin.flow.router.WildcardParameter
import support.addSortableColumn
import support.createPrimaryButton
import support.createPrimarySmallButton
import support.createSearchBar

@Route(value = "admin/selections", layout = BaseLayout::class)
class SelectionView(
    private val applicantService: ApplicantService,
    private val recruitmentService: RecruitmentService
) : VerticalLayout(), HasUrlParameter<Long> {
    private var recruitmentId: Long = 0L

    private fun createTitle(): Component {
        return HorizontalLayout(H1(recruitmentService.getById(recruitmentId).title)).apply {
            setSizeFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        }
    }

    private fun createMenu(): Component {
        return HorizontalLayout(
            createSearchBar {
                removeAll()
                add(createTitle(), createMenu(), createGrid(getApplicants(applicantService.getByName(it))))
            },
            createPrimaryButton("다운로드") {
                // Todo: 엑셀 다운로드
            }
        ).apply {
            setSizeFull()
            isSpacing = false
            justifyContentMode = FlexComponent.JustifyContentMode.BETWEEN
        }
    }

    private fun getApplicants(applicant: Applicant?): List<Applicant> {
        return when (applicant) {
            null -> emptyList()
            else -> listOf(applicant)
        }
    }

    private fun createGrid(applicants: List<Applicant>): Component {
        return Grid<Applicant>(10).apply {
            addSortableColumn("이름", Applicant::name)
            addSortableColumn("이메일", Applicant::email)
            addSortableColumn("전화번호", Applicant::phoneNumber)
            addSortableColumn("성별", Applicant::gender)
            addSortableColumn("생년월일", Applicant::birthday)
            addColumn(createButtonRenderer()).apply { isAutoWidth = true }
            setItems(applicants)
        }
    }

    private fun createButtonRenderer(): Renderer<Applicant> {
        return ComponentRenderer<Component, Applicant> { applicant ->
            createPrimarySmallButton("지원서") { applicant.id }
        }
    }

    override fun setParameter(event: BeforeEvent?, @WildcardParameter parameter: Long) {
        this.recruitmentId = parameter
        add(createTitle(), createMenu(), createGrid(applicantService.findAll()))
    }
}
